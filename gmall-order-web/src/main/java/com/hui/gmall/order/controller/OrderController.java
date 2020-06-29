package com.hui.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.CartInfo;
import com.hui.gmall.bean.OrderDetail;
import com.hui.gmall.bean.OrderInfo;
import com.hui.gmall.bean.UserAddress;
import com.hui.gmall.conf.LoginRequire;
import com.hui.gmall.service.CartService;
import com.hui.gmall.service.OrderService;
import com.hui.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/7 12:39
 */
@Controller
public class OrderController {
    @Reference
    private UserService userService;
    @Reference
    private CartService cartService;
    @Reference
    private OrderService orderService;


    @RequestMapping("trade")
    @LoginRequire(autoRedirect = true)
    public String trade(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        List<UserAddress> userAddressList = userService.getUserAddressList(userId);
        request.setAttribute("userAddressList", userAddressList);
        //展示送货清单，数据来源：勾选购物车
        List<CartInfo> cartInfoList = cartService.getCartCheckedList(userId);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartInfo cartInfo : cartInfoList) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());
            orderDetail.setImgUrl(cartInfo.getImgUrl());
            orderDetail.setSkuNum(cartInfo.getSkuNum());
            orderDetail.setOrderPrice(cartInfo.getCartPrice());
            orderDetailList.add(orderDetail);
        }
        request.setAttribute("orderDetailList", orderDetailList);
        //总金额
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderDetailList(orderDetailList);
        orderInfo.sumTotalAmount();
        request.setAttribute("totalAmount", orderInfo.getTotalAmount());
        //保存流水号至页面
        String tradeNo = orderService.getTradeNo(userId);
        request.setAttribute("tradeNo", tradeNo);
        return "trade";
    }

    @RequestMapping("submitOrder")
    @LoginRequire(autoRedirect = true)
    public String submitOrder(HttpServletRequest request, OrderInfo orderInfo) {
        orderInfo.setUserId((String) request.getAttribute("userId"));
        //在保存订单信息之前先判断是否表单重复提交
        boolean result = orderService.checkTradeCode(orderInfo.getUserId(), request.getParameter("tradeNo"));
        if (!result) {
            //重复提交
            request.setAttribute("errMsg", "订单已提交，不能重复提交！");
            return "tradeFail";
        }
        //验证之后删除流水号
        orderService.delTradeCode(orderInfo.getUserId());
        String orderId = orderService.saveOrder(orderInfo);
        return "redirect://payment.gmall.com/index?orderId=" + orderId;
    }
}
