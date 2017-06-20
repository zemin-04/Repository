package com.shunhai.skipcloud.web.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公共视图控制器
 **/
@Controller
public class CommonController {
    /**
     * 首页
     *
     * @param request
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request) {
    	System.out.println("index");
        return "index";
    }

}