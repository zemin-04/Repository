package com.shunhai.skipcloud.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.shunhai.skipcloud.core.util.IPUtils;
import com.shunhai.skipcloud.core.util.SimpleMailSender;
import com.shunhai.skipcloud.web.model.IpLocation;
import com.shunhai.skipcloud.web.model.User;
import com.shunhai.skipcloud.web.security.PermissionSign;
import com.shunhai.skipcloud.web.security.RoleSign;
import com.shunhai.skipcloud.web.service.IpLocationService;
import com.shunhai.skipcloud.web.service.UserService;

/**
 * 用户控制器
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;


    @Resource(name="shiroEhcacheManager")
    private CacheManager cacheManager;
    private Cache<String,String> changePasswordCache;

    @Resource
    private IpLocationService ipLocationService;


    /**
     * 用户登录
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
    	//验证码校验
    	String vcode = request.getParameter("vcode");
    	if(!vcode.toLowerCase().equals(WebUtils.getSessionAttribute(request, "vcode"))){
    		model.addAttribute("error", "验证码错误 ！");
    		return "login";
    	}
    	String error = null;
        try {
        	Subject subject = SecurityUtils.getSubject();
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
                return "redirect:/";
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "用户参数错误！");
                return "login";
            }

            //设置rememberMe
            UsernamePasswordToken shiroToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
            shiroToken.setRememberMe(user.isRememberMe());

            // 身份验证
            subject.login(shiroToken);
            // 验证成功在Session中保存用户信息
            final User authUserInfo = userService.selectByUsername(user.getUsername());
            WebUtils.setSessionAttribute(request, "userInfo", authUserInfo);

            String ip = IPUtils.getIP(request);
            System.out.println(ip);
            IpLocation ipLocation = ipLocationService.selectByIp(ip);
            System.out.println(ipLocation.getId()+":"+ipLocation.getAddress()+"--"+ipLocation.getCompany());

        } catch ( LockedAccountException e ) {
    	    error = "登录失败3次，账户已被锁定 ，请3分钟后再试！";
        } catch ( DisabledAccountException e ) {
    	    error = "该账户已被禁用 ，请联系管理员！";
        } catch (UnknownAccountException e) {
        	error = "该账户不存在 ！";
        } catch (IncorrectCredentialsException e) {
        	error = "用户名或密码错误 ！";
        } catch (AuthenticationException e) {
            error = "其他错误："+e.getMessage()+"！";
        }
        if(null != error){
        	model.addAttribute("error", error);
        	return "login";
        }else{
        	return "redirect:/";
        }
    }

    /**
     * 用户登出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("userInfo");
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    /**
     * 基于角色 标识的权限控制案例
     */
    @RequestMapping(value = "/admin")
    @ResponseBody
    @RequiresRoles(value = RoleSign.ADMIN)
    public String admin() {
        return "拥有admin角色,能访问";
    }

    /**
     * 基于权限标识的权限控制案例
     */
    @RequestMapping(value = "/create")
    @ResponseBody
    @RequiresPermissions(value = PermissionSign.USER_CREATE)
    public String create() {
        return "拥有user:create权限,能访问";
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
    	System.out.println("register:"+user.getPassword());
    	user.setState("正常");
    	user.setCreateTime(new Date());
    	if(userService.insertUser(user))
    		return "login";
    	else
    		return "error";
    }

    /**
     * 验证账号唯一
     */
    @RequestMapping(value = "/checkUsername", method = RequestMethod.POST)
    @ResponseBody
    public List<Integer> checkUsername(String username){
    	List<Integer> ma = new ArrayList<Integer>();
    	boolean isTrue = userService.checkusername(username);
    	if(isTrue)
    		ma.add(1);
    	else
    		ma.add(2);

    	return ma;
    }

    /**
     * 验证邮箱唯一
     */
    @RequestMapping(value = "/checkEmail", method = RequestMethod.POST)
    @ResponseBody
    public List<Integer> checkEmail(String email){
    	//String a[] =username.split("=");
    	List<Integer> emai = new ArrayList<Integer>();
    	boolean isTrue = userService.checkEmail(email);
    	if(isTrue)
    		emai.add(1);
    	else
    		emai.add(2);

    	return emai;
    }

    /**
     * 根据邮箱修改密码
     */
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public String forgetPassword(@Valid User user, BindingResult result, Model model, HttpServletRequest reques){
    	System.out.println("邮箱传过来了："+user.getEmail());
    	//这里返回的id可能有bug
    	//Long id  = userService.selectByEmail(user.getEmail());
    	SimpleMailSender sms = new SimpleMailSender();
    	sms.send(user.getEmail(),SimpleMailSender.CHANGES_PWD);
    	changePasswordCache = cacheManager.getCache("changePasswordCache");
    	changePasswordCache.put(user.getEmail(), "email");
    	//sms.properties.setProperty("toEmailAddress", user.getEmail());
    	//sms.send(user.getEmail(),SimpleMailSender.CHANGES_PWD);
    	return "login";
    }
    /**
     * 跳到changPassword页面
     */
    @RequestMapping(value = "/cp", method = RequestMethod.GET)
    public String cp(@Valid User user, BindingResult result, Model model, HttpServletRequest request){
    	String email = request.getParameter("zemin");
    	/*System.out.println("配置获取的id传过来了："+id);
    	user.setId(id);;*/
    	if(null==email||email.trim().equals("")){
    		model.addAttribute("error","此链接参数已被破坏，请重新输入");
    	}
    	user.setEmail(Base64.decodeToString(email));
    	return "changePassword";
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/changPassword", method = RequestMethod.POST)
    public String changPassword(@Valid User user, BindingResult result, Model model, HttpServletRequest reques){
    	String email = changePasswordCache.get(user.getEmail());
    	if(null != email && email.equals("email")){
    		userService.changePassword(user);
    		return "login";
    	}else{
    		model.addAttribute("error", "此链接已失效，请重新申请改密！");
    		return "changePassword";
    	}
    }

    /**
     * 显示所有用户
     */
    @RequestMapping(value = "/userList")
    public String userList(Model model){
    	List<User> userList = userService.selectList();
    	model.addAttribute("userList", userList);
    	return "user/userList";
    }

    /**
     * 根据用户名查找用户
     */
    @RequestMapping(value = "/findUser")
    public String findUser(User user, Model model){
    	List<User> userList = userService.selectAllByUsername(user.getUsername());
    	if(null == userList || userList.size()==0){
    		model.addAttribute("error", "没有匹配的用户名 ！");
    	}else{
    		model.addAttribute("userList", userList);
    	}
    	return "user/userList";
    }
}
