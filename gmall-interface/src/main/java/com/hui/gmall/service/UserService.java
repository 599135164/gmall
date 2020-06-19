package com.hui.gmall.service;

import com.hui.gmall.bean.UserAddress;
import com.hui.gmall.bean.UserInfo;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/6 23:20
 */
public interface UserService {
    /**
     * 查询所有用户信息数据
     * @return
     */
    List<UserInfo> findAll();

    /**
     * 根据用户id查询用户地址列表
     * @param userId
     * @return
     */
    List<UserAddress> getUserAddressList(String userId);

    /**
     * 登录接口
     * @param userInfo
     * @return
     */
    UserInfo login(UserInfo userInfo);
}
