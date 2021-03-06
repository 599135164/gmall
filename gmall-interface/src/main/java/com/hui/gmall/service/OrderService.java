package com.hui.gmall.service;

import com.hui.gmall.bean.OrderInfo;
import com.hui.gmall.bean.enmus.ProcessStatus;

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

    /**
     * 核验库存是否充足
     * @param skuId
     * @param skuNum
     * @return
     */
    boolean checkStock(String skuId, Integer skuNum);

    /**
     * 根据 订单id 来获取订单信息
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfoById(String orderId);

    void updateOrderStatus(String orderId, ProcessStatus processStatus);

    void sendOrderStatus(String orderId);

}
