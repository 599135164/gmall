package com.hui.gmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/16 0:13
 */
@Data
public class SkuLsInfo implements Serializable {

    String id;

    BigDecimal price;

    String skuName;

    String catalog3Id;

    String skuDefaultImg;

    Long hotScore=0L;

    List<SkuLsAttrValue> skuAttrValueList;
}
