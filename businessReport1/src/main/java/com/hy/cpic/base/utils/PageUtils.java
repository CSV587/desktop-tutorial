package com.hy.cpic.base.utils;

import com.hy.cpic.base.page.BasePage;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 19:50 2018/9/6
 * @ Description ：页面类工具
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class PageUtils {

    public static void randomId(List<? extends BasePage> pages){
        if(CollectionUtils.isNotEmpty(pages)){
            for(BasePage basePage : pages){
                basePage.setId(UUID.randomUUID().toString());
            }
        }
    }

}
