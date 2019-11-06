package com.hy.iom.base.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ Author     ：wellhor Zhao
 * @ Date       ：Created in 11:29 2018/8/23
 * @ Description：功能：excel模板设置
 * @ Modified By：
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelAnnotation {
    /**
     * .
     * Excel列ID(Excel列排序序号)
     */
    int id();

    /**
     * .
     * Excel列名
     */
    String[] name();

    /**
     * .
     * Excel列宽
     */
    int width() default 5000;
}
