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
}