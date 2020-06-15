package com.hui.gmall.service;

import com.hui.gmall.bean.SkuLsInfo;

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
}
