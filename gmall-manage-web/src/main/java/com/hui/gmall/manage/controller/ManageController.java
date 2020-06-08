package com.hui.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.BaseCatalog1;
import com.hui.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/8 17:56
 */
@Controller
public class ManageController {
    @Reference
    private ManageService manageService;
    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<BaseCatalog1> getCatalog(){
        return manageService.getCatalog1();
    }
}
