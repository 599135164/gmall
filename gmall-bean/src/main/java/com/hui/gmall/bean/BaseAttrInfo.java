package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/8 18:15
 */
@Data
public class BaseAttrInfo implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY) //获取主键自增
    private String id;
    @Column
    private String attrName;
    @Column
    private String catalog3Id;

    //BaseAttrValue集合
    @Transient
    private List<BaseAttrValue> attrValueList;
}

