package com.shunhai.skipcloud.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shunhai.skipcloud.core.generic.GenericDao;
import com.shunhai.skipcloud.core.generic.impl.GenericServiceImpl;
import com.shunhai.skipcloud.web.dao.RoleMapper;
import com.shunhai.skipcloud.web.model.Role;
import com.shunhai.skipcloud.web.service.RoleService;


/**
 * 角色Service实现类
 */
@Service
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public GenericDao<Role, Long> getDao() {
        return roleMapper;
    }

    @Override
    public List<Role> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

}
