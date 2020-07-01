package com.hui.gmall.payment.service.impl;

import com.hui.gmall.bean.PaymentInfo;
import com.hui.gmall.payment.mapper.PaymentInfoMapper;
import com.hui.gmall.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/30 14:24
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }
}
