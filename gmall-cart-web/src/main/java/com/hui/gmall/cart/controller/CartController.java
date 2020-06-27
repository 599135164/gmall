package com.hui.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.SkuInfo;
import com.hui.gmall.cart.handler.CartCookieHandler;
import com.hui.gmall.conf.LoginRequire;
import com.hui.gmall.service.CartService;
import com.hui.gmall.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/20 16:12
 */
@Controller
public class CartController {

    @Reference
    private CartService cartService;
    @Reference
    private ManageService manageService;

    @Autowired
    private CartCookieHandler cartCookieHandler;

    @RequestMapping("addToCart")
    @LoginRequire(autoRedirect = false)  //无需登录鉴权
    public String addToCart(HttpServletRequest request, HttpServletResponse response) {
        String skuId = request.getParameter("skuId");
        String userId = (String) request.getAttribute("userId");
        Integer skuNum = Integer.parseInt(request.getParameter("skuNum"));
        if (null != userId && userId.length() != 0) {
            //用户已登录
            cartService.addToCart(skuId, userId, skuNum);
        } else {
            //用户未登录
            cartCookieHandler.addToCart(request, response, skuId, userId, skuNum);
        }
        //根据skuId查询skuInfo
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        request.setAttribute("skuInfo", skuInfo);
        request.setAttribute("skuNum", skuNum);
        return "success";
    }
}
