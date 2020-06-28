package com.hui.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hui.gmall.bean.CartInfo;
import com.hui.gmall.bean.SkuInfo;
import com.hui.gmall.cart.constant.CartConst;
import com.hui.gmall.cart.mapper.CartInfoMapper;
import com.hui.gmall.config.RedisUtil;
import com.hui.gmall.service.CartService;
import com.hui.gmall.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/21 0:29
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Reference
    private ManageService manageService;

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void addToCart(String skuId, String userId, Integer skuNum) {
        //判断购物车中是否曾经已经添加了这个商品，如果添加过了就相加
        //查询购物车中是否有重复商品
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        cartInfo.setSkuId(skuId);
        CartInfo cartInfoExist = cartInfoMapper.selectOne(cartInfo);
        if (null != cartInfoExist) {
            //更新数量和价格
            cartInfoExist.setSkuNum(cartInfoExist.getSkuNum() + skuNum);
            cartInfoExist.setSkuPrice(cartInfoExist.getCartPrice());
            cartInfoMapper.updateByPrimaryKeySelective(cartInfoExist);
        } else {
            //insert
            SkuInfo skuInfo = manageService.getSkuInfo(skuId);
            CartInfo cartInfo1 = new CartInfo();
            cartInfo1.setSkuId(skuId);
            cartInfo1.setCartPrice(skuInfo.getPrice());
            cartInfo1.setSkuPrice(skuInfo.getPrice());
            cartInfo1.setSkuName(skuInfo.getSkuName());
            cartInfo1.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo1.setUserId(userId);
            cartInfo1.setSkuNum(skuNum);
            cartInfoMapper.insertSelective(cartInfo1);
            cartInfoExist = cartInfo1;
        }
        //缓存
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //定义key
            String userCaryKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CART_KEY_SUFFIX;
            jedis.hset(userCaryKey, skuId, JSON.toJSONString(cartInfoExist));
            String userInfoKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USERINFOKEY_SUFFIX;
            //取用户登录的过期时间
            Long ttl = jedis.ttl(userInfoKey);
            jedis.expire(userCaryKey, ttl.intValue());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
    }

    @Override
    public List<CartInfo> getCartList(String userId) {
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String userCaryKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CART_KEY_SUFFIX;
            List<String> cartJsons = jedis.hvals(userCaryKey);
            if (null != cartJsons && cartJsons.size() > 0) {
                List<CartInfo> cartInfoList = new ArrayList<>();
                for (String cartJson : cartJsons) {
                    CartInfo cartInfo = JSON.parseObject(cartJson, CartInfo.class);
                    cartInfoList.add(cartInfo);
                }
                //排序
                cartInfoList.sort((o1, o2) -> Long.compare(Long.parseLong(o2.getId()), Long.parseLong(o1.getId())));
                return cartInfoList;
            } else return loadCartCache(userId, jedis); //从数据库中读取数据并添加到缓存
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
        return null;
    }

    @Override
    public List<CartInfo> mergeToCartList(List<CartInfo> cartListCK, String userId) {
        List<CartInfo> cartInfoListDB = cartInfoMapper.selectCartListWithCurPrice(userId);
//        思路：用数据库中的购物车列表与传递过来的cookie里的购物车列表循环匹配。
//        能匹配上的数量相加
//        匹配不上的插入到数据库中。
//        最后重新加载缓存
        for (CartInfo cartInfoCK : cartListCK) {
            boolean isMatch = false;
            for (CartInfo cartInfoDB : cartInfoListDB) {
                if (cartInfoCK.getSkuId().equals(cartInfoDB.getSkuId())) {
                    cartInfoDB.setSkuNum(cartInfoCK.getSkuNum() + cartInfoDB.getSkuNum());
                    cartInfoMapper.updateByPrimaryKeySelective(cartInfoDB);
                    isMatch = true;
                }
            }
            //没有匹配上
            if (!isMatch) {
                //添加至数据库
                cartInfoCK.setUserId(userId);
                cartInfoMapper.insertSelective(cartInfoCK);
            }
        }
        Jedis jedis = null;
        List<CartInfo> cartInfoList = null;
        try {
            jedis = redisUtil.getJedis();
            cartInfoList = loadCartCache(userId, jedis);
            for (CartInfo cartInfo : cartInfoList) {
                for (CartInfo info : cartListCK) {
                    if (cartInfo.getSkuId().equals(info.getSkuId())) {
                        // 只有被勾选的才会进行更改
                        if (info.getIsChecked().equals("1")) {
                            cartInfo.setIsChecked(info.getIsChecked());
                            // 更新redis中的isChecked
                            checkCart(cartInfo.getSkuId(), info.getIsChecked(), userId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
        return cartInfoList;
    }

    @Override
    public void checkCart(String skuId, String isChecked, String userId) {
        String userCaryKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CART_KEY_SUFFIX;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            //先获取购物车,然后修改
            String cartJson = jedis.hget(userCaryKey, skuId);
            CartInfo cartInfo = JSON.parseObject(cartJson, CartInfo.class);
            cartInfo.setIsChecked(isChecked);
            String jsonString = JSON.toJSONString(cartInfo);
            jedis.hset(userCaryKey, skuId, jsonString);
            String userCheckedKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CHECKED_KEY_SUFFIX;
            //新建一个用于存储已勾选的商品购物车
            if (isChecked.equals("1")) jedis.hset(userCheckedKey, skuId, jsonString);
            else jedis.hdel(userCheckedKey, skuId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
    }

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {
        //定义key
        String userCheckedKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CHECKED_KEY_SUFFIX;
        Jedis jedis = null;
        List<CartInfo> cartInfoList = new ArrayList<>();
        try {
            jedis = redisUtil.getJedis();
            List<String> cartCheckedList = jedis.hvals(userCheckedKey);
            if (null != cartCheckedList && cartCheckedList.size() > 0) {
                for (String cartInfoJson : cartCheckedList) {
                    CartInfo cartInfo = JSON.parseObject(cartInfoJson, CartInfo.class);
                    cartInfoList.add(cartInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != jedis) jedis.close();
        }
        return cartInfoList;
    }

    private List<CartInfo> loadCartCache(String userId, Jedis Jedis) {
        List<CartInfo> cartInfoList = cartInfoMapper.selectCartListWithCurPrice(userId);
        if (null == cartInfoList || cartInfoList.size() == 0) return null;
        String userCaryKey = CartConst.USER_KEY_PREFIX + userId + CartConst.USER_CART_KEY_SUFFIX;
        HashMap<String, String> map = new HashMap<>(cartInfoList.size());
        for (CartInfo cartInfo : cartInfoList) map.put(cartInfo.getSkuId(), JSON.toJSONString(cartInfo));
        Jedis.hmset(userCaryKey, map);
        return cartInfoList;
    }
}
