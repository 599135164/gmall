package com.hui.gmall.order.mq;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.enmus.ProcessStatus;
import com.hui.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/7/1 22:16
 */
@Component
public class OrderConsumer {
    @Reference
    OrderService orderService;

    //获取消息队列中的数据
    @JmsListener(destination = "PAYMENT_RESULT_QUEUE", containerFactory = "jmsQueueListener")
    public void consumerPaymentResult(MapMessage mapMessage) throws JMSException {
        String orderId = mapMessage.getString("orderId");
        String result = mapMessage.getString("result");
        if ("success".equals(result)) {
            orderService.updateOrderStatus(orderId, ProcessStatus.PAID);
            //发送消息通知库存
            orderService.sendOrderStatus(orderId);
            orderService.updateOrderStatus(orderId, ProcessStatus.NOTIFIED_WARE);
        }
    }

    //获取仓库发来的消息
    @JmsListener(destination = "SKU_DEDUCT_QUEUE", containerFactory = "jmsQueueListener")
    public void consumeSkuDeduct(MapMessage mapMessage) throws JMSException {
        String orderId = mapMessage.getString("orderId");
        String status = mapMessage.getString("status");
        if ("DEDUCTED".equals(status)) {
            orderService.updateOrderStatus(orderId, ProcessStatus.WAITING_DELEVER);
        } else {
            orderService.updateOrderStatus(orderId, ProcessStatus.STOCK_EXCEPTION);
        }
    }

}
