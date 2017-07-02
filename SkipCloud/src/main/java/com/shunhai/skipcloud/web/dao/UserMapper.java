package com.shunhai.skipcloud.web.dao;

import java.util.List;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.web.model.User;

public interface UserMapper extends GenericDao<User, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	User authentication(User user);

	User selectByUsername(String username);

	List<User> checkUsername(String username);

	List<User> checkByEmail(String email);

	void changPassword(User user);

	List<User> selectAllByUsername(String username);

}