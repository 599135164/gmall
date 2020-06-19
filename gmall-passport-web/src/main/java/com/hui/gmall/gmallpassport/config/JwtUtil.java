package com.hui.gmall.gmallpassport.config;


import io.jsonwebtoken.*;
import java.util.Map;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/19 1:20
 */


public class JwtUtil {
    /**
     * 签发 token
     * @param key 公共部分
     * @param param 私有部分
     * @param salt 签名部分
     * @return
     */
    public static String encode(String key,Map<String,Object> param,String salt){
        if(salt!=null){
            key+=salt;
        }
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key);

        jwtBuilder = jwtBuilder.setClaims(param);

        String token = jwtBuilder.compact();
        return token;

    }

    /**
     *  解密 token
     * @param token token
     * @param key 公共部分
     * @param salt 签名部分
     * @return
     */
    public  static Map<String,Object> decode(String token , String key, String salt){
        Claims claims=null;
        if (salt!=null){
            key+=salt;
        }
        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
            return null;
        }
        return  claims;
    }

}
