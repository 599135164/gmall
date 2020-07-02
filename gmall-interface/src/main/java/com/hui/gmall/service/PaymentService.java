package com.hui.gmall.service;

import com.hui.gmall.bean.PaymentInfo;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/30 14:22
 */
public interface PaymentService {
    void savePaymentInfo(PaymentInfo paymentInfo);

    PaymentInfo getPaymentInfo(PaymentInfo paymentInfoQuery);

    void updatePaymentInfo(String out_trade_no, PaymentInfo paymentInfo);

    void sendPaymentResult(PaymentInfo paymentInfo, String result);

    boolean checkPayment(PaymentInfo paymentInfoQuery);

    void sendDelayPaymentResult(String outTradeNo,int delaySec ,int checkCount);
}
