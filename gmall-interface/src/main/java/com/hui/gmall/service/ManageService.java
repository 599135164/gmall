package com.hui.gmall.service;

import com.hui.gmall.bean.*;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/8 18:21
 */
public interface ManageService {
    /**
     * 获取所有的一级分类数据
     * @return
     */
    List<BaseCatalog1> getCatalog1();

    /**
     * 根据一级分类ID获取所有二级分类
     * @param catalog1Id
     * @return
     */
    List<BaseCatalog2> getCatalog2(String catalog1Id);

    /**
     * 根据二级分类ID获取所有三级分类
     * @param catalog2Id
     * @return
     */
     List<BaseCatalog3> getCatalog3(String catalog2Id);

    /**
     * 根据三级分类ID获取平台属性
     * @param catalog3Id
     * @return
     */
     List<BaseAttrInfo> getAttrList(String catalog3Id);

    /**
     * 保存平台属性数据
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据 BaseAttrValue.attrId 获取 属性值 AttrValueList 集合
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueList(String attrId);

    /**
     * 根据 spuInfo 对象属性获取 SpuInfo 集合
     * @param spuInfo
     * @return
     */
    List<SpuInfo> getSpuList(SpuInfo spuInfo);

    /**
     * 获取所有销售属性数据
     * @return
     */
    List<BaseSaleAttr> getBaseSaleAttrList();

    /**
     *保存 spu 信息
     * @param spuInfo
     */
     void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据 spuId 来获取 spu的图片集合
     * @param spuId
     * @return
     */
    List<SpuImage> getSpuImageList(String spuId);

    /**
     * 根据 spuId 来获取销售属性
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    /**
     * 保存当前 sku 信息
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 根据 skuId 查询 skuInfo
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfo(String skuId);

    /**
     * 根据 skuId 查询 skuImage 集合
     * @param skuId
     * @return
     */
    List<SkuImage> getSkuImageBySkuId(String skuId);

    /**
     * 根据 skuId ，spuId 查询销售属性集合
     * @param skuInfo
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo);

    /**
     * 根据 spuId 来获取销售属性值集合
     * @param spuId
     * @return
     */
    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);
}
