package com.hui.gmall.cart.mapper;

import com.hui.gmall.bean.CartInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/20 22:38
 */
public interface CartInfoMapper extends Mapper<CartInfo> {
    /**
     * 根据 userId 查询购物车集合
     * @param userId
     * @return
     */
    List<CartInfo> selectCartListWithCurPrice(String userId);
}
