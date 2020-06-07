package com.hui.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.UserAddress;
import com.hui.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/7 12:39
 */
@Controller
public class OrderController {
    @Reference
    private UserService userService;
//    @RequestMapping("trade")
//    public String trade(){
//        //返回一个视图名称叫index，html
//        return "index";
//    }
    @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> trade(String userId) {
        //返回一个视图名称叫index，html
        return userService.getUserAddressList(userId);
    }
}
