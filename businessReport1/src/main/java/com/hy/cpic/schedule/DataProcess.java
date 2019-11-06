package com.hy.cpic.schedule;

import java.util.List;

/**
 * @ Author     ：wellhor Zhao
 * @ Date       ：Created in 16:10 2018/9/3
 * @ Description：数据分析处理类
 * @ Modified By：
 * @Version: 1.0
 */
public interface DataProcess {

    boolean process(String jsonStr, List list);

}
