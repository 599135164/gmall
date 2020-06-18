package com.hui.gmall.list.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.BaseAttrInfo;
import com.hui.gmall.bean.BaseAttrValue;
import com.hui.gmall.bean.SkuLsParams;
import com.hui.gmall.bean.SkuLsResult;
import com.hui.gmall.service.ListService;
import com.hui.gmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/17 14:53
 */
@Controller
public class ListController {
    @Reference
    private ListService listService;

    @Reference
    private ManageService manageService;

    @RequestMapping("list.html")
    public String listData(SkuLsParams skuLsParams, HttpServletRequest request) {
        skuLsParams.setPageSize(12);
        SkuLsResult skuLsResult = listService.search(skuLsParams);
        //显示商品数据
        //平台属性和平台属性值查询
        List<String> attrValueIdList = skuLsResult.getAttrValueIdList();
        List<BaseAttrInfo> baseAttrInfoList = manageService.getAttrList(attrValueIdList);

        //编写一个方法来判断url后面的参数条件
        String urlParam = this.makeUrlParam(skuLsParams);
        //定义一个面包屑集合
        ArrayList<BaseAttrValue> baseAttrValueArrayList = new ArrayList<>();

        //使用迭代器 这里的逻辑有点复杂，难！
        for (Iterator<BaseAttrInfo> attrInfoIterator = baseAttrInfoList.iterator(); attrInfoIterator.hasNext(); ) {
            BaseAttrInfo baseAttrInfo = attrInfoIterator.next();
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            for (BaseAttrValue baseAttrValue : attrValueList) {
                if (null != skuLsParams.getValueId() && skuLsParams.getValueId().length > 0) {
                    for (String valueId : skuLsParams.getValueId()) {
                        if (valueId.equals(baseAttrValue.getId())) {
                            attrInfoIterator.remove();
                            BaseAttrValue baseAttrValueed = new BaseAttrValue();
                            baseAttrValueed.setValueName(baseAttrInfo.getAttrName() + ": " + baseAttrValue.getValueName());

                            String newUrlParam = makeUrlParam(skuLsParams, valueId);
                            baseAttrValueed.setUrlParam(newUrlParam);
                            baseAttrValueArrayList.add(baseAttrValueed);
                        }
                    }
                }
            }
        }
        request.setAttribute("totalPages", skuLsResult.getTotalPages());
        request.setAttribute("pageNo",skuLsParams.getPageNo());
        request.setAttribute("urlParam", urlParam);
        request.setAttribute("keyword", skuLsParams.getKeyword());
        //保存一个面包屑
        request.setAttribute("baseAttrValueArrayList", baseAttrValueArrayList);
        request.setAttribute("baseAttrInfoList", baseAttrInfoList);
        request.setAttribute("skuLsInfoList", skuLsResult.getSkuLsInfoList());

        return "list";
    }

    private String makeUrlParam(SkuLsParams skuLsParams, String... excludeValueIds) {
        String urlParam = "";
        //拼接 Keyword
        if (null != skuLsParams.getKeyword() && skuLsParams.getKeyword().length() > 0)
            urlParam += "keyword=" + skuLsParams.getKeyword();
        //拼接 catalog3Id
        if (null != skuLsParams.getCatalog3Id() && skuLsParams.getCatalog3Id().length() > 0) {
            if (urlParam.length() > 0) urlParam += "&";
            urlParam += "catalog3Id=" + skuLsParams.getCatalog3Id();
        }
        // 拼接 平台属性值 id
        if (null != skuLsParams.getValueId() && skuLsParams.getValueId().length > 0) {
            for (String valueId : skuLsParams.getValueId()) {
                if (excludeValueIds != null && excludeValueIds.length > 0) {
                    String excludeValueId = excludeValueIds[0];
                    if (excludeValueId.equals(valueId)) {
                        // 跳出代码，后面的参数则不会继续追加【后续代码不会执行】
                        // 不能写break；如果写了break；其他条件则无法拼接！
                        continue;
                    }
                }
                if (urlParam.length() > 0) urlParam += "&";
                urlParam += "valueId=" + valueId;
            }
        }
        return urlParam;
    }
}
