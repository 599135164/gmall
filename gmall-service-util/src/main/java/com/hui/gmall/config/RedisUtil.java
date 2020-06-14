package com.hui.gmall.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/14 11:52
 */
public class RedisUtil {
    //创建连接池
    private JedisPool jedisPool;

    //创建连接池配置类
    public void initJedisPool(String host, int port,int database) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //设置连接池的最大数
        jedisPoolConfig.setMaxTotal(200);
        //设置最大等待时间
        jedisPoolConfig.setMaxWaitMillis(10 * 1000);
        //设置最小剩余数
        jedisPoolConfig.setMinIdle(10);
        // 在获取连接时，检查是否有效
        jedisPoolConfig.setTestOnBorrow(true);
        //开启获取连接池的缓冲
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(jedisPoolConfig, host, port, 20 * 1000);
    }
    public Jedis getJedis(){
        return jedisPool.getResource();
    }
}
