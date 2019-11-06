package com.hy.cpic.base.excel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:11 2018/8/23
 * @ Description ：报表工具类
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class ReportUtils {

    /**
     * .
     * 获取时间格式
     *
     * @return 时间格式字符串
     */
    public static String getTimeString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

}
