package com.hui.gmall.gmallpassport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hui.gmall.bean.UserInfo;
import com.hui.gmall.gmallpassport.config.JwtUtil;
import com.hui.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/18 23:29
 */
@Controller
public class PassPortController {
    @Value("${token.key}")
    private String key;

    @Reference
    private UserService userService;

    @RequestMapping("index")
    public String index(HttpServletRequest request) {
        //获取originUrl
        String originUrl = request.getParameter("originUrl");
        request.setAttribute("originUrl", originUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo, HttpServletRequest request) {
        if (null != userInfo) {
            //调用登录方法
            UserInfo info = userService.login(userInfo);
            if (null != info) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", info.getId());
                map.put("nickName", info.getNickName());
                return JwtUtil.encode(key, map, request.getHeader("X-forwarded-for"));
            }
        }
        return "fail";
    }

    @RequestMapping("verify")
    public String verify(HttpServletRequest request) {
        //String salt = request.getHeader("X-forwarded-for");
        String token = request.getParameter("token");
        String salt = request.getParameter("salt");
        Map<String, Object> map = JwtUtil.decode(token, key, salt);
        if (null != map && map.size() > 0) {
            String userId = (String) map.get("userId");
            UserInfo userInfo = userService.verify(userId);
            if (null != userInfo) return "success";
        }
        return "fail";
    }
}
