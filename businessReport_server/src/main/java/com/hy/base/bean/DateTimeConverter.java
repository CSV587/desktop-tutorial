package com.hy.base.bean;

import org.apache.commons.beanutils.Converter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:47 2018/8/30
 * @ Description ：日期转换
 * @ Modified By ：
 * @ Version     ：
 */
public class DateTimeConverter implements Converter {
    /**
     * .
     * 日期格式 yyyy-MM-dd
     */
    private static final String DATE = "yyyy-MM-dd";
    /**
     * .
     * 日期格式 yyyy-MM-dd HH:mm:ss
     */
    private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * .
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    private static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * .
     * 转换
     *
     * @param type  类
     * @param value 对象实例
     * @return 对象实例
     */
    @Override
    public Object convert(final Class type, final Object value) {
        return toDate(type, value);
    }

    /**
     * .
     * 日期转换
     *
     * @param type  类
     * @param value 对象实例
     * @return 对象实例
     */
    private static Object toDate(final Class type, final Object value) {
        if (value == null || "".equals(value)) {
            return null;
        }
        if (value instanceof String) {
            String dateValue = value.toString().trim();
            int length = dateValue.length();
            if (type.equals(java.util.Date.class)) {
                try {
                    DateFormat formatter;
                    if (length <= DATE.length()) {
                        formatter = new SimpleDateFormat(DATE,
                            new DateFormatSymbols(Locale.CHINA));
                        return formatter.parse(dateValue);
                    }
                    if (length <= DATETIME.length()) {
                        formatter = new SimpleDateFormat(DATETIME,
                            new DateFormatSymbols(Locale.CHINA));
                        return formatter.parse(dateValue);
                    }
                    if (length <= TIMESTAMP.length()) {
                        formatter = new SimpleDateFormat(TIMESTAMP,
                            new DateFormatSymbols(Locale.CHINA));
                        return formatter.parse(dateValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}

