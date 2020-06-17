package com.hui.gmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/17 13:53
 */
@Data
public class SkuLsParams implements Serializable {

    String  keyword;

    String catalog3Id;

    String[] valueId;

    int pageNo=1;

    int pageSize=20;
}


