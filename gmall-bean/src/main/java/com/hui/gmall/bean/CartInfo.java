package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/20 22:33
 */
@Data
public class CartInfo implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    String id;
    @Column
    String userId;
    @Column
    String skuId;
    @Column
    BigDecimal cartPrice;
    @Column
    //商品数量
    Integer skuNum;
    @Column
    String imgUrl;
    @Column
    String skuName;

    // 实时价格
    @Transient
    BigDecimal skuPrice;
    // 下订单的时候，商品是否勾选
    @Transient
    String isChecked="0";
}
