package com.hy.base.utils;

import com.hy.base.excel.ExcelAnnotation;
import com.hy.base.page.BasePage;
import com.hy.base.page.TableHead;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 19:50 2018/9/6
 * @ Description ：页面类工具
 * @ Modified By ：
 * @ Version     ：1.0
 */
public final class PageUtils {
    /**
     * .
     * 工具类隐藏构造函数
     */
    private PageUtils() {

    }

    /**
     * .
     * 随机id
     *
     * @param pages page列表
     */
    public static void randomId(final List<? extends BasePage> pages) {
        if (CollectionUtils.isNotEmpty(pages)) {
            for (BasePage basePage : pages) {
                basePage.setId(UUID.randomUUID().toString());
            }
        }
    }


    /**
     * .
     * 获取Excel的title对象
     *
     * @param aClass 类class
     * @return Excel title数列
     */
    public static List<TableHead> getExcelTitle(final Class aClass) {
        List<Field> fieldList = FieldUtil.getAllFieldByClass(aClass);
        List<TableHead> tableHeads = new ArrayList<>();
        for (Field field : fieldList) {
            ExcelAnnotation excelAnnotation
                = field.getAnnotation(
                ExcelAnnotation.class);
            if (excelAnnotation != null) {
                TableHead tableHead
                    = new TableHead(field.getName(),
                    excelAnnotation.name()[0]);
                tableHeads.add(tableHead);
            }
        }
        return tableHeads;
    }
}
