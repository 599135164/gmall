package com.hui.gmall.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/7 12:39
 */
@Controller
public class OrderController {
    @RequestMapping("trade")
    public String trade(){
        //返回一个视图名称叫index，html
        return "index";
    }
}
