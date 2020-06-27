package com.hui.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hui.gmall.bean.SkuImage;
import com.hui.gmall.bean.SkuInfo;
import com.hui.gmall.bean.SkuSaleAttrValue;
import com.hui.gmall.bean.SpuSaleAttr;
import com.hui.gmall.conf.LoginRequire;
import com.hui.gmall.service.ListService;
import com.hui.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/11 23:12
 */
@Controller
public class ItemController {

    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

    @RequestMapping("{skuId}.html")
    //@LoginRequire
    public String skuInfoPage(@PathVariable(value = "skuId") String skuId, HttpServletRequest request) {
        //根据skuId获取数据
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        //显示图片列表
        List<SkuImage> skuImageList = manageService.getSkuImageBySkuId(skuId);
        request.setAttribute("skuImageList", skuImageList);
        //查询销售属性和销售属性值集合
        List<SpuSaleAttr> spuSaleAttrList = manageService.getSpuSaleAttrListCheckBySku(skuInfo);
        //获取销售属性值 Id 的集合
        List<SkuSaleAttrValue> skuSaleAttrValues = manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        //遍历集合拼接字符串
        String key = "";
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < skuSaleAttrValues.size(); i++) {
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValues.get(i);
            if (key.length() != 0) key += "|";
            key += skuSaleAttrValue.getSaleAttrValueId();
            //当本次的skuId与下一次的skuId不一致时候，停止拼接
            if (i + 1 == skuSaleAttrValues.size() || !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValues.get(i + 1).getSkuId())) {
                map.put(key, skuSaleAttrValue.getSkuId());
                key = "";
            }
        }
        //讲map转换为json字符串
        String valuesSkuJson = JSON.toJSONString(map);
        //保存到作用域
        request.setAttribute("valuesSkuJson", valuesSkuJson);
        request.setAttribute("spuSaleAttrList", spuSaleAttrList);
        request.setAttribute("skuInfo", skuInfo);
        listService.incrHotScore(skuId);
        return "item";
    }


}
