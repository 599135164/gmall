package com.hui.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.CartInfo;
import com.hui.gmall.bean.SkuInfo;
import com.hui.gmall.cart.handler.CartCookieHandler;
import com.hui.gmall.conf.LoginRequire;
import com.hui.gmall.service.CartService;
import com.hui.gmall.service.ManageService;
import com.hui.gmall.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
        if (null != userId && userId.length() != 0) cartService.addToCart(skuId, userId, skuNum);  //用户已登录
        else cartCookieHandler.addToCart(request, response, skuId, userId, skuNum);  //用户未登录
        //根据skuId查询skuInfo
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        request.setAttribute("skuInfo", skuInfo);
        request.setAttribute("skuNum", skuNum);
        return "success";
    }

    @RequestMapping("cartList")
    @LoginRequire(autoRedirect = false)  //无需登录鉴权
    public String cartList(HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getAttribute("userId");
        //查询购物车信息
        List<CartInfo> cartList = null;
        if (null != userId && userId.length() != 0) {
            //用户已登录,合并购物车
            List<CartInfo> cartListCK = cartCookieHandler.getCartList(request);
            if (null != cartListCK && cartListCK.size() > 0) {
                //合并购物车
                cartList = cartService.mergeToCartList(cartListCK, userId);
                //删除 cookie 中的购物车
                cartCookieHandler.deleteCartCookie(request, response);
            } else cartList = cartService.getCartList(userId);
        } else cartList = cartCookieHandler.getCartList(request); //用户未登录
        request.setAttribute("cartList", cartList);
        return "cartList";
    }

    @RequestMapping("checkCart")
    @LoginRequire(autoRedirect = false)  //无需登录鉴权
    @ResponseBody
    public void checkCart(HttpServletRequest request, HttpServletResponse response) {
        String isChecked = request.getParameter("isChecked");
        String skuId = request.getParameter("skuId");
        String userId = (String) request.getAttribute("userId");
        if (userId != null) {
            cartService.checkCart(skuId, isChecked, userId);
        } else {
            cartCookieHandler.checkCart(request, response, skuId, isChecked);
        }
    }

    @RequestMapping("toTrade")
    @LoginRequire(autoRedirect = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response) {
        String userId = (String) request.getAttribute("userId");
        List<CartInfo> cookieHandlerCartList = cartCookieHandler.getCartList(request);
        if (cookieHandlerCartList != null && cookieHandlerCartList.size() > 0) {
            cartService.mergeToCartList(cookieHandlerCartList, userId);
            cartCookieHandler.deleteCartCookie(request, response);
        }
        return "redirect://order.gmall.com/trade";
    }

    @RequestMapping("submitOrder")
    @LoginRequire(autoRedirect = true)
    public String submitOrder(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return "redirect://payment.gmall.com/index?orderId=" + orderId;
    }
}
