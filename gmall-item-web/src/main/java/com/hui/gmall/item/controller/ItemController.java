package com.hui.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.SkuImage;
import com.hui.gmall.bean.SkuInfo;
import com.hui.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("{skuId}.html")
    public String skuInfoPage(@PathVariable(value = "skuId") String skuId, HttpServletRequest request) {
        //根据skuId获取数据
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        //显示图片列表
       List<SkuImage> skuImageList= manageService.getSkuImageBySkuId(skuId);
       request.setAttribute("skuImageList",skuImageList);
        //保存到作用域
        request.setAttribute("skuInfo", skuInfo);
        return "item";
    }


}
