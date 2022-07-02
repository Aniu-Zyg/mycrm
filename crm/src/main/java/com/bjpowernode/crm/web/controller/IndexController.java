package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    // "/"表示根目录：WEB-INF
    @RequestMapping("/")
    public String index(){
        // 请求转发
        return "index";
    }
}
