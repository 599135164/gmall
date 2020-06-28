package com.hui.gmall.cart.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hui.gmall.bean.CartInfo;
import com.hui.gmall.bean.SkuInfo;
import com.hui.gmall.service.ManageService;
import com.hui.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/21 16:20
 */
@Component
public class CartCookieHandler {

    // 定义购物车名称
    private final String cookieCartName = "CART";
    // 设置cookie 过期时间
    private int COOKIE_CART_MAXAGE = 7 * 24 * 3600;

    @Reference
    private ManageService manageService;


    /**
     * 未登录添加至购物车
     *
     * @param request
     * @param response
     * @param skuId
     * @param userId
     * @param skuNum
     */
    public void addToCart(HttpServletRequest request, HttpServletResponse response, String skuId, String userId, Integer skuNum) {
        //产看购物车中是否已有商品
        //有则相加
        //没有则直接添加
        //从cookie中取出购物车数据
        String cartJson = CookieUtil.getCookieValue(request, cookieCartName, true);
        List<CartInfo> cartInfoList = new ArrayList<>();
        boolean ifExist = false;
        if (StringUtils.isNotEmpty(cartJson)) {
            cartInfoList = JSON.parseArray(cartJson, CartInfo.class);
            for (CartInfo cartInfo : cartInfoList) {
                //存在商品
                if (cartInfo.getSkuId().equals(skuId)) {
                    cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
                    cartInfo.setSkuPrice(cartInfo.getCartPrice());
                    ifExist = true;
                    break;
                }
            }
        }
        if (!ifExist) {
            //把商品信息取出来，新增到购物车
            SkuInfo skuInfo = manageService.getSkuInfo(skuId);
            CartInfo cartInfo = new CartInfo();

            cartInfo.setSkuId(skuId);
            cartInfo.setCartPrice(skuInfo.getPrice());
            cartInfo.setSkuPrice(skuInfo.getPrice());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());

            cartInfo.setUserId(userId);
            cartInfo.setSkuNum(skuNum);
            cartInfoList.add(cartInfo);
        }
        CookieUtil.setCookie(request, response, cookieCartName, JSON.toJSONString(cartInfoList), COOKIE_CART_MAXAGE, true);
    }

    public List<CartInfo> getCartList(HttpServletRequest request) {
        String cookieValue = CookieUtil.getCookieValue(request, cookieCartName, true);
        if (StringUtils.isNotEmpty(cookieValue))
            return JSON.parseArray(cookieValue, CartInfo.class);
        return null;
    }

    public void deleteCartCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, cookieCartName);
    }

    public void checkCart(HttpServletRequest request, HttpServletResponse response, String skuId, String isChecked) {
        //取出购物车中的商品
        List<CartInfo> cartList = getCartList(request);
        for (CartInfo cartInfo : cartList) {
            if (cartInfo.getSkuId().equals(skuId)) cartInfo.setIsChecked(isChecked);
        }
        CookieUtil.setCookie(request, response, cookieCartName, JSON.toJSONString(cartList), COOKIE_CART_MAXAGE, true);
    }
}
