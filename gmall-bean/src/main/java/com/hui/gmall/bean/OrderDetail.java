package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/28 13:54
 */
@Data
public class OrderDetail implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String orderId;
    @Column
    private String skuId;
    @Column
    private String skuName;
    @Column
    private String imgUrl;
    @Column
    private BigDecimal orderPrice;
    @Column
    private Integer skuNum;

    @Transient
    private String hasStock;
}
