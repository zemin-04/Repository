package com.shunhai.skipcloud.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.Md5Hash;
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

	@Override
	public boolean insertUser(User user) {
		String cryptedPwd = new Md5Hash(user.getPassword()).toString();
		user.setPassword(cryptedPwd);
		int index = userMapper.insert(user);
		if(index>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean checkusername(String username) {
		List<User> user = userMapper.checkUsername(username);
		if(user.isEmpty())
			return false;
		else
			return true;
	}

	@Override
	public boolean checkEmail(String email) {
		List<User> user = userMapper.checkByEmail(email);
		if(user.isEmpty())
			return false;
		else
			return true;
	}
}