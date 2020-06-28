package com.hui.gmall.service;

import com.hui.gmall.bean.CartInfo;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/20 22:39
 */
public interface CartService {
    /**
     * 登录下添加购物车
     * @param skuId
     * @param userId
     * @param skuNum
     */
    void addToCart(String skuId,String userId,Integer skuNum);

    /**
     * 登录下根据用户id查询购物车内容
     * @param userId
     * @return
     */
    List<CartInfo> getCartList(String userId);

    /**
     * 根据 userId 查询购物车内容与 cookie 中的购物车合并
     * @param cartListCK
     * @param userId
     * @return
     */
    List<CartInfo> mergeToCartList(List<CartInfo> cartListCK, String userId);

    /**
     * 登录状态下勾选购物车，修改redis isChecked
     * @param skuId
     * @param isChecked
     * @param userId
     */
    void checkCart(String skuId, String isChecked, String userId);
}
