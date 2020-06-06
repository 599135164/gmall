package com.hui.gmall.user.controller;

import com.hui.gmall.bean.UserInfo;
import com.hui.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/7 0:47
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("findAll")
    public List<UserInfo> findAll() {
        return userService.findAll();
    }
}
