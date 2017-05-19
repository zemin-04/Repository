package com.shunhai.skipcloud.web.dao;

import java.util.List;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.web.model.Role;

public interface RoleMapper extends GenericDao<Role, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

	List<Role> selectRolesByUserId(Long userId);
}