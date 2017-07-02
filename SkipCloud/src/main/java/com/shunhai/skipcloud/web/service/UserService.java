package com.shunhai.skipcloud.web.service;

import java.util.List;

import com.shunhai.skipcloud.core.generic.GenericService;
import com.shunhai.skipcloud.web.model.User;

/**
 * 用户 业务 接口
 **/
public interface UserService extends GenericService<User, Long> {

    /**
     * 用户登录验证
     * @param user
     * @return
     */
    User authentication(User user);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User selectByUsername(String username);

    /**
     * 根据用户名模糊查询用户
     * @param username
     * @return
     */
    List<User> selectAllByUsername(String username);

    /**
     * 用户注册
     * @param user
     * @return true代表插入成功 ;false代表插入失败
     */
    boolean insertUser(User user);

    /**
     * 检查用户是否存在
     * @param username
     */
	boolean checkusername(String username);

	/**
     * 检查邮箱是否存在
     * @param username
     */
	boolean checkEmail(String email);

	/**
	 * 根据邮箱来修改密码
	 * @param user
	 */
	void changePassword(User user);

	Long selectByEmail(String email);

}
