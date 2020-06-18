package com.hui.gmall.service;

import com.hui.gmall.bean.SkuLsInfo;
import com.hui.gmall.bean.SkuLsParams;
import com.hui.gmall.bean.SkuLsResult;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/16 0:18
 */
public interface ListService {
    /**
     * 保存数据到 es 中
     */
    void saveSkuLsInfo(SkuLsInfo skuLsInfo);

    /**
     * 基于 DSL 查询
     * @param skuLsParams
     * @return
     */
    SkuLsResult search(SkuLsParams skuLsParams);

    /**
     * 依据 skuId 增加商品热度
     * @param skuId
     */
    public void incrHotScore(String skuId);
}
