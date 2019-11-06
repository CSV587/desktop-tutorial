package com.hy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-09
 * user: lxg
 * package_name: com.hy.util
 */
public final class DateUtil {

    /**
     * .
     * 工具类私有化构造函数
     */
    private DateUtil() {

    }

    /**
     * .
     * 转换指定时间格式文本
     *
     * @param sourceDate   原始文本
     * @param sourceFormat 原始格式文本
     * @param targetFormat 目的格式文本
     * @return 目的文本
     * @throws ParseException 转换异常
     */
    public static String conversion(
        final String sourceDate,
        final String sourceFormat,
        final String targetFormat)
        throws ParseException {
        SimpleDateFormat source = new SimpleDateFormat(sourceFormat);
        Date date = source.parse(sourceDate);
        SimpleDateFormat target = new SimpleDateFormat(targetFormat);
        return target.format(date);
    }


    /**
     * .
     * 指定格式字符串转为时间对象
     *
     * @param sourceDate   原始字符串
     * @param sourceFormat 格式化文本
     * @return 时间对象
     * @throws ParseException 转换异常
     */
    public static Date conversion(
        final String sourceDate,
        final String sourceFormat)
        throws ParseException {
        SimpleDateFormat source = new SimpleDateFormat(sourceFormat);
        return source.parse(sourceDate);
    }
}
