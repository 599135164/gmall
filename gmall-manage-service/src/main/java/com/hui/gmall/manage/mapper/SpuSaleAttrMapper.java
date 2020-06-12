package com.hui.gmall.manage.mapper;

import com.hui.gmall.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/10 21:56
 */
public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    /**
     * 根据spuId查询销售属性集合
     * 需要使用SpuSaleAttrMapper.xml
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> selectSpuSaleAttrList(String spuId);

    /**
     * 根据 skuId ，spuId 查询销售属性集合
     * @param skuId
     * @param spuId
     * @return
     */

    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(String skuId, String spuId);
}

