package com.shunhai.skipcloud.web.security;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.shunhai.skipcloud.web.model.Permission;
import com.shunhai.skipcloud.web.model.Role;
import com.shunhai.skipcloud.web.model.User;
import com.shunhai.skipcloud.web.service.PermissionService;
import com.shunhai.skipcloud.web.service.RoleService;
import com.shunhai.skipcloud.web.service.UserService;

/**
 * 用户身份验证,授权 Realm 组件
 **/
@Component(value = "securityRealm")
public class SecurityRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Resource(name="shiroEhcacheManager")
    private CacheManager cacheManager;

    private Cache<String,AtomicInteger> userRetryCache;

    /**
     * 权限检查
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = String.valueOf(principals.getPrimaryPrincipal());
        final User user = userService.selectByUsername(username);
        final List<Role> roleInfos = roleService.selectRolesByUserId(user.getId());
        for (Role role : roleInfos) {
            // 添加角色
            System.err.println(role);
            authorizationInfo.addRole(role.getRoleSign());

            final List<Permission> permissions = permissionService.selectPermissionsByRoleId(role.getId());
            for (Permission permission : permissions) {
                // 添加权限
                System.err.println(permission);
                authorizationInfo.addStringPermission(permission.getPermissionSign());
            }
        }
        return authorizationInfo;
    }

    /**
     * 登录验证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
    	md5CredentialsMatcher.setHashIterations(1024);
    	setCredentialsMatcher(md5CredentialsMatcher);
    	String username = String.valueOf(token.getPrincipal());
        // 通过数据库进行验证
        final User user = userService.selectByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("该帐号不存在！");
        }else if("禁用".equals(user.getState())){
        	throw new DisabledAccountException("该账户已被禁用 ，请联系管理员！");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(username), getName());
        return authenticationInfo;
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token,
    		AuthenticationInfo info) throws AuthenticationException {
    	userRetryCache = cacheManager.getCache("userRetryCache");
    	String username = String.valueOf(token.getPrincipal());
    	AtomicInteger retryCount  = userRetryCache.get(username);
    	if(null == retryCount){
    		retryCount = new AtomicInteger(0);
    		userRetryCache.put(username, retryCount);
    	}
    	if (retryCount.incrementAndGet() > 3) {
    		System.out.println("登录失败"+retryCount+"次");
            throw new LockedAccountException("登录失败3次，账户已被锁定 ，请3分钟后再试！");
        }
    	super.assertCredentialsMatch(token, info);
    	userRetryCache.remove(username);
    }
}
