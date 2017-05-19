package com.shunhai.skipcloud.web.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.core.generic.impl.GenericServiceImpl;
import com.shunhai.skipcloud.web.dao.UserMapper;
import com.shunhai.skipcloud.web.model.User;
import com.shunhai.skipcloud.web.service.UserService;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int insert(User model) {
        return userMapper.insertSelective(model);
    }

    @Override
    public int update(User model) {
        return userMapper.updateByPrimaryKeySelective(model);
    }

    @Override
    public int delete(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User authentication(User user) {
        return userMapper.authentication(user);
    }

    @Override
    public User selectById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public GenericDao<User, Long> getDao() {
        return userMapper;
    }

	@Override
	public User selectByUsername(String username) {
		return userMapper.selectByUsername(username);
	}



}