package com.hui.gmall.service;

import com.hui.gmall.bean.CartInfo;

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

}
