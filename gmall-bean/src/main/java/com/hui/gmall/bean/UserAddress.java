package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/7 13:08
 */
@Data
public class UserAddress implements Serializable {
    @Column
    @Id
    private String id;
    @Column
    private String userAddress;
    @Column
    private String userId;
    @Column
    private String consignee;
    @Column
    private String phoneNum;
    @Column
    private String isDefault;
}
