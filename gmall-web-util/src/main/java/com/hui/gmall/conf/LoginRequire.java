package com.hui.gmall.conf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shenhui
 * @version 1.0
 * @date 2020/6/19 16:14
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequire {
    /**
     *  是否需要登录 默认值为 true
     * @return
     */
    boolean autoRedirect() default true;
}
