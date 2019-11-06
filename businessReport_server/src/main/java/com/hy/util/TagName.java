package com.hy.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-09
 * user: lxg
 * package_name: com.hy.util
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TagName {
    /**
     * .
     * 标签名
     *
     * @return 字符串
     */
    String name();
}
