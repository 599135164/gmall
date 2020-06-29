package com.hui.gmall.service;

import com.hui.gmall.bean.OrderInfo;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/28 23:31
 */
public interface OrderService {
    /**
     * 保存订单信息
     * @param orderInfo
     * @return
     */
    String saveOrder(OrderInfo orderInfo);

    /**
     * 生成流水号
     * @param userId
     * @return
     */
    String getTradeNo(String userId);

    /**
     * 验证流水号
     * @param userId
     * @param tradeCodeNo
     * @return
     */
    boolean checkTradeCode(String userId,String tradeCodeNo);

    /**
     * 删除流水号
     * @param userId
     */
    void  delTradeCode(String userId);
}
