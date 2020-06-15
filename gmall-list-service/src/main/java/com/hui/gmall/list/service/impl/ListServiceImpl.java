package com.hui.gmall.list.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hui.gmall.bean.SkuLsInfo;
import com.hui.gmall.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/16 0:20
 */
@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private JestClient jestClient;

    public static final String ES_INDEX="gmall";

    public static final String ES_TYPE="SkuInfo";

    @Override
    public void saveSkuLsInfo(SkuLsInfo skuLsInfo) {
        //定义保存动作
        Index index = new Index.Builder(skuLsInfo).index(ES_INDEX).type(ES_TYPE).id(skuLsInfo.getId()).build();
        try {
            jestClient.execute(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
