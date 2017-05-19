package com.shunhai.skipcloud.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.core.generic.impl.GenericServiceImpl;
import com.shunhai.skipcloud.web.dao.PermissionMapper;
import com.shunhai.skipcloud.web.model.Permission;
import com.shunhai.skipcloud.web.service.PermissionService;


/**
 * 权限Service实现类
 */
@Service
public class PermissionServiceImpl extends GenericServiceImpl<Permission, Long> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;


    @Override
    public GenericDao<Permission, Long> getDao() {
        return permissionMapper;
    }

    @Override
    public List<Permission> selectPermissionsByRoleId(Long roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }
}
