package com.hy.iom.load.utils;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 11:23 2018/8/22
 * @ Description ：数据过滤器 自定义逻辑决定是否保留
 * @ Modified By ：
 * @ Version     ：1.0
 */
public interface DataFilter {

    boolean filter(Object... objects);

}
