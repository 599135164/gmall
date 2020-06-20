package com.hui.gmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/20 16:12
 */
@Controller
public class CartController {

    @RequestMapping("addToCart")
    public String addToCart(){
        return "success";
    }
}
