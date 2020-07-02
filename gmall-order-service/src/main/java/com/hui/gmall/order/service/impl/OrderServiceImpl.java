package com.hui.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hui.gmall.bean.OrderDetail;
import com.hui.gmall.bean.OrderInfo;
import com.hui.gmall.bean.enmus.OrderStatus;
import com.hui.gmall.bean.enmus.ProcessStatus;
import com.hui.gmall.config.ActiveMQUtil;
import com.hui.gmall.config.RedisUtil;
import com.hui.gmall.order.mapper.OrderDetailMapper;
import com.hui.gmall.order.mapper.OrderInfoMapper;
import com.hui.gmall.service.OrderService;
import com.hui.gmall.util.HttpClientUtil;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.jms.*;
import javax.jms.Queue;
import java.util.*;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/28 23:32
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ActiveMQUtil activeMQUtil;

    @Override
    @Transactional
    public String saveOrder(OrderInfo orderInfo) {
        //需要 总金额，订单状态，第三方交易编号，创建时间，过期时间，进程状态
        orderInfo.sumTotalAmount();
        orderInfo.setCreateTime(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        orderInfo.setExpireTime(calendar.getTime());
        orderInfo.setOrderStatus(OrderStatus.UNPAID);
        String outTradeNo = "SHENHUI" + System.currentTimeMillis() + "" + new Random().nextInt(1000);
        orderInfo.setOutTradeNo(outTradeNo);
        orderInfo.setProcessStatus(ProcessStatus.UNPAID);
        orderInfoMapper.insertSelective(orderInfo);
        for (OrderDetail orderDetail : orderInfo.getOrderDetailList()) {
            orderDetail.setOrderId(orderInfo.getId());
            orderDetailMapper.insertSelective(orderDetail);
        }
        return orderInfo.getId();
    }

    @Override
    public String getTradeNo(String userId) {
        Jedis jedis = null;
        String tradeCode = "";
        try {
            jedis = redisUtil.getJedis();
            //定义流水号的key
            String tradeNoKey = "user:" + userId + ":tradeCode";
            //定义流水号
            tradeCode = UUID.randomUUID().toString();
            jedis.setex(tradeNoKey, 10 * 60, tradeCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
        return tradeCode;
    }

    @Override
    public boolean checkTradeCode(String userId, String tradeCodeNo) {
        Jedis jedis = null;
        String tradeCode = "";
        try {
            jedis = redisUtil.getJedis();
            //需要获取 Redis 中的流水号
            String tradeNoKey = "user:" + userId + ":tradeCode";
            tradeCode = jedis.get(tradeNoKey);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
        return tradeCodeNo.equals(tradeCode);
    }

    @Override
    public void delTradeCode(String userId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //需要获取 Redis 中的流水号
            String tradeNoKey = "user:" + userId + ":tradeCode";
            jedis.del(tradeNoKey);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
    }

    @Override
    public boolean checkStock(String skuId, Integer skuNum) {
        //远程调用接口
        String result = HttpClientUtil.doGet("http://www.gware.com/hasStock?skuId=" + skuId + "&num=" + skuNum);
        return "1".equals(result);
    }

    @Override
    public OrderInfo getOrderInfoById(String orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.select(orderDetail);

        orderInfo.setOrderDetailList(orderDetailList);
        return orderInfo;

    }

    @Override
    public void updateOrderStatus(String orderId, ProcessStatus processStatus) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setProcessStatus(processStatus);
        orderInfo.setOrderStatus(processStatus.getOrderStatus());
        orderInfoMapper.updateByPrimaryKeySelective(orderInfo);

    }

    public void sendOrderStatus(String orderId) {
        Connection connection = activeMQUtil.getConnection();
        String orderJson = initWareOrder(orderId);
        try {
            connection.start();
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Queue order_result_queue = session.createQueue("ORDER_RESULT_QUEUE");
            MessageProducer producer = session.createProducer(order_result_queue);

            ActiveMQTextMessage textMessage = new ActiveMQTextMessage();
            textMessage.setText(orderJson);
            producer.send(textMessage);
            session.commit();
            session.close();
            producer.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public String initWareOrder(String orderId) {
        OrderInfo orderInfo = getOrderInfoById(orderId);
        Map map = initWareOrder(orderInfo);
        return JSON.toJSONString(map);
    }

    // 设置初始化仓库信息方法
    public Map initWareOrder(OrderInfo orderInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        map.put("consignee", orderInfo.getConsignee());
        map.put("consigneeTel", orderInfo.getConsigneeTel());
        map.put("orderComment", orderInfo.getOrderComment());
        map.put("orderBody", "测试用例");
        map.put("deliveryAddress", orderInfo.getDeliveryAddress());
        map.put("paymentWay", "2");
        map.put("wareId", orderInfo.getWareId());

        // 组合json
        List detailList = new ArrayList();
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            Map detailMap = new HashMap();
            detailMap.put("skuId", orderDetail.getSkuId());
            detailMap.put("skuName", orderDetail.getSkuName());
            detailMap.put("skuNum", orderDetail.getSkuNum());
            detailList.add(detailMap);
        }
        map.put("details", detailList);
        return map;
    }


}
