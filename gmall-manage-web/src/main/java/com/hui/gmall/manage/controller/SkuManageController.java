package com.hui.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.*;
import com.hui.gmall.service.ListService;
import com.hui.gmall.service.ManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/11 0:13
 */
@RestController
@CrossOrigin
public class SkuManageController {
    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

    @RequestMapping("spuImageList")
    public List<SpuImage> spuImageList(String spuId){
        return   manageService.getSpuImageList(spuId);
    }

    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        return manageService.getSpuSaleAttrList(spuId);
    }

    @RequestMapping("saveSkuInfo")
    public void saveSkuInfo(@RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo(skuInfo);
    }

    @RequestMapping("onSale")
    public void onSale(String skuId){
        //创建一个SkuLsInfo对象
        SkuLsInfo skuLsInfo = new SkuLsInfo();
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        //属性拷贝
        BeanUtils.copyProperties(skuInfo,skuLsInfo);
        listService.saveSkuLsInfo(skuLsInfo);
    }
}
