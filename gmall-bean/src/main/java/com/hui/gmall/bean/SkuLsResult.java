package com.hui.gmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/17 13:54
 */
@Data
public class SkuLsResult implements Serializable {

    List<SkuLsInfo> skuLsInfoList;

    long total;

    long totalPages;

    List<String> attrValueIdList;
}
