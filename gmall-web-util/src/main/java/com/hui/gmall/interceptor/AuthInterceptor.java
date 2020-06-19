package com.hui.gmall.interceptor;

import com.alibaba.fastjson.JSON;
import com.hui.gmall.util.CookieUtil;
import com.hui.gmall.util.WebConst;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/19 14:34
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    //进入控制器之前拦截
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //获取token
        String token = httpServletRequest.getParameter("newToken");
        //将 token 放入 cookie 中
        if (null != token)
            CookieUtil.setCookie(httpServletRequest, httpServletResponse, "token", token, WebConst.COOKIE_MAXAGE, false);
        //如果 token 为空的话就从 cookie 中获取 token
        if (null == token)
            token = CookieUtil.getCookieValue(httpServletRequest, "token", false);
        if (null != token) {
            //解密 token 获取 nickName
            Map map = getUserMapByToken(token);
            String nickName = (String) map.get("nickName");
            httpServletRequest.setAttribute("nickName", nickName);
        }
        return true;
    }

    private Map getUserMapByToken(String token) {
        String tokenUserInfo = StringUtils.substringBetween(token, ".");
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] tokenBytes = base64UrlCodec.decode(tokenUserInfo);
        String tokenJson = null;
        try {
            tokenJson = new String(tokenBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(tokenJson, Map.class);

    }

    //进入控制器之后，视图渲染之前拦截
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //视图渲染之后拦截
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
