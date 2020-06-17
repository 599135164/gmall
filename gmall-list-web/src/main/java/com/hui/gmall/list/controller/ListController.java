package com.hui.gmall.list.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hui.gmall.bean.SkuLsParams;
import com.hui.gmall.bean.SkuLsResult;
import com.hui.gmall.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/17 14:53
 */
@Controller
public class ListController {
    @Reference
    private ListService listService;

    @RequestMapping("list.html")
    @ResponseBody
    public String listData(SkuLsParams skuLsParams) {
        SkuLsResult skuLsResult = listService.search(skuLsParams);
        return JSON.toJSONString(skuLsResult);
    }
}
