package com.shunhai.skipcloud.web.dao;

import java.util.List;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.web.model.Permission;

public interface PermissionMapper extends GenericDao<Permission, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

	List<Permission> selectPermissionsByRoleId(Long roleId);
}