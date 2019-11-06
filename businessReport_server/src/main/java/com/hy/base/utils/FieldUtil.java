package com.hy.base.utils;

import com.hy.base.excel.ExcelAnnotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-16
 * user: lxg
 * package_name: com.hy.base.utils
 */
public final class FieldUtil {

    /**
     * .
     * 工具类隐藏构造类
     */
    private FieldUtil() {

    }

    /**
     * .
     * 获取Class 所有Field
     *
     * @param aClass class
     * @return 所有Field
     */
    public static List<Field> getAllFieldByClass(final Class aClass) {
        List<Field> fields = new ArrayList<>();
        Class tmpClass = aClass;
        while (tmpClass != null) {
            fields.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                fieldList.add(field);
            }
        }
        // 按照id进行排序
        fieldList.sort(
            Comparator.comparingInt(
                f -> f.getAnnotation(ExcelAnnotation.class).id()
            ));
        return fieldList;
    }
}
