package com.hui.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hui.gmall.bean.UserAddress;
import com.hui.gmall.bean.UserInfo;
import com.hui.gmall.config.RedisUtil;
import com.hui.gmall.service.UserService;
import com.hui.gmall.user.mapper.UserAddressMapper;
import com.hui.gmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;


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

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private RedisUtil redisUtil;

    public String userKey_prefix = "user:";
    public String userinfoKey_suffix = ":info";
    public int userKey_timeOut = 60 * 60 * 24;


    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        //密码需要加密
        String passwd = userInfo.getPasswd();
        String newPassword = DigestUtils.md5DigestAsHex(passwd.getBytes());
        //查询DB是否有当前用户
        userInfo.setPasswd(newPassword);
        UserInfo info = userInfoMapper.selectOne(userInfo);
        //有当前用户将用户信息存储进Redis
        if (null != info) {
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                // 呐  第一步 要起一个 key  user:userId:info
                String userKey = userKey_prefix + info.getId() + userinfoKey_suffix;
                jedis.setex(userKey, userKey_timeOut, JSON.toJSONString(info));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != jedis) jedis.close();
            }
        }
        return info;
    }

    @Override
    public UserInfo verify(String userId) {
        Jedis jedis =null;
        try {
            jedis = redisUtil.getJedis();
            String key=userKey_prefix+userId+userinfoKey_suffix;
            String userJson = jedis.get(key);
            if (!StringUtils.isEmpty(userJson)) return JSON.parseObject(userJson, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
        return null;
    }
}
