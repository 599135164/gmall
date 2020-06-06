package com.hui.gmall.user.service.impl;

import com.hui.gmall.bean.UserInfo;
import com.hui.gmall.service.UserService;
import com.hui.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/7 0:42
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }
}