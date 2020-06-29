package com.hui.gmall.order.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hui.gmall.bean.OrderDetail;
import com.hui.gmall.bean.OrderInfo;
import com.hui.gmall.bean.enmus.OrderStatus;
import com.hui.gmall.bean.enmus.ProcessStatus;
import com.hui.gmall.config.RedisUtil;
import com.hui.gmall.order.mapper.OrderDetailMapper;
import com.hui.gmall.order.mapper.OrderInfoMapper;
import com.hui.gmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

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
}
