package com.hui.gmall.gmallpassport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/18 23:29
 */
@Controller
public class PassPortController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}
