package com.hui.gmall.manage.mapper;

import com.hui.gmall.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/11 13:02
 */
public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {
    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(String spuId);
}

