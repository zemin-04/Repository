package com.shunhai.skipcloud.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 权限控制器
 **/
@Controller
@RequestMapping(value = "/permission")
public class PermissionController {

    /**
     * 权限列表页
     */
    @RequestMapping("/permissionList")
    public String dashboard() {
        return "permission/permissionList";
    }



}
