package com.shunhai.skipcloud.web.service;

import com.shunhai.skipcloud.core.generic.GenericService;
import com.shunhai.skipcloud.web.model.User;

/**
 * 用户 业务 接口
 **/
public interface UserService extends GenericService<User, Long> {

    /**
     * 用户验证
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
}
