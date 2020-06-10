package com.hui.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.SpuInfo;
import com.hui.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/9 16:53
 */
@RestController
@CrossOrigin
public class SpuManageController {
    @Reference
    private ManageService manageService;

    @RequestMapping("spuList")
    public List<SpuInfo> spuList(SpuInfo spuInfo) {
        return manageService.getSpuList(spuInfo);
    }
}
