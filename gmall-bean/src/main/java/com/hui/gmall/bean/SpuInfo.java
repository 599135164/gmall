package com.hui.gmall.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/9 16:36
 */
@Data
public class SpuInfo implements Serializable {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String spuName;

    @Column
    private String description;

    @Column
    private  String catalog3Id;

    @Transient
    private List<SpuSaleAttr> spuSaleAttrList;

    @Transient
    private List<SpuImage> spuImageList;


}
