package com.hy.iom.base.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ Author     ：wellhor Zhao
 * @ Date       ：Created in 11:29 2018/8/23
 * @ Description：功能：excel导出时 枚举值的中文注释
 * @ Modified By：
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelEnumAnnotation {

    /**
     * .
     * 导出excel枚举描述
     */
    String name();

}
