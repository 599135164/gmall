package com.hui.gmall.service;

import com.hui.gmall.bean.UserInfo;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/6 23:20
 */
public interface UserService {
    /**
     * 查询所有数据
     * @return
     */
    List<UserInfo> findAll();
}
