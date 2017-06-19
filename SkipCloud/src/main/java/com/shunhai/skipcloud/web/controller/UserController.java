package com.shunhai.skipcloud.web.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shunhai.skipcloud.web.model.User;
import com.shunhai.skipcloud.web.security.PermissionSign;
import com.shunhai.skipcloud.web.security.RoleSign;
import com.shunhai.skipcloud.web.service.UserService;

/**
 * 用户控制器
 **/
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param user
     * @param result
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@Valid User user, BindingResult result, Model model, HttpServletRequest request) {
        try {
            Subject subject = SecurityUtils.getSubject();
            System.out.println("已经进入userController");
            // 已登陆则 跳到首页
            if (subject.isAuthenticated()) {
            	System.out.println("已经登陆");
                return "redirect:/";
            }
            if (result.hasErrors()) {
                model.addAttribute("error", "参数错误！");
                System.out.println("参数有误");
                return "login";
            }
            System.out.println(user.getUsername()+"正在进行身份认证,密码为："+user.getPassword());
            String passwordNew = new Md5Hash(user.getPassword()).toString();
            System.out.println("后端加密为："+passwordNew);
            // 身份验证
            subject.login(new UsernamePasswordToken(user.getUsername(), passwordNew));
            // 验证成功在Session中保存用户信息
            final User authUserInfo = userService.selectByUsername(user.getUsername());
            request.getSession().setAttribute("userInfo", authUserInfo);
            System.out.println(user.getUsername()+"登陆成功,密码为："+authUserInfo.getPassword());
        } catch (AuthenticationException e) {
            // 身份验证失败
            model.addAttribute("error", "用户名或密码错误 ！");
            System.out.println("用户名或密码错误");
            return "login";
        }
        return "redirect:/";
    }

    /**
     * 用户登出
     *
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
    	System.out.println("注册时SHA256加密后密码为："+user.getPassword()+" 姓名是："+user.getFullname());
    	user.setState("正常");
    	user.setCreateTime(new Date());
    	if(userService.insertUser(user))
    		return "login";
    	else
    		return "error";
    }
}
