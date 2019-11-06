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
public @interface DateFormat {
    /**
     * .
     * 原始格式
     *
     * @return 原始格式
     */
    String source();

    /**
     * .
     * 目的格式
     *
     * @return 目的格式
     */
    String target();
}
