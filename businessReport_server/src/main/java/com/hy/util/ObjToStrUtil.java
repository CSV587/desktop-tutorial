package com.hy.util;

import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-22
 * user: lxg
 * package_name: com.hy.util
 */
public final class ObjToStrUtil {

    /**
     * .
     * 工具类隐藏构造函数
     */
    private ObjToStrUtil() {

    }

    /**
     * .
     * 对象转为一行字符串,指定分隔符,以注解Order排序
     *
     * @param source    来源对象
     * @param separator 分隔符
     * @return 字符串
     */
    public static String objToStr(final Object source, final String separator) {
        List<Field> fields = new ArrayList<>();
        Class tmpClass = source.getClass();
        while (tmpClass != null) {
            fields.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Order.class)) {
                fieldList.add(field);
            }
        }
        fieldList.sort(
            Comparator.comparingInt(
                f -> f.getAnnotation(Order.class).value()
            ));
        StringBuilder builder = new StringBuilder();
        for (Field f : fieldList) {
            f.setAccessible(true);
            try {
                if (builder.length() > 0) {
                    builder.append(separator);
                }
                if (f.get(source) != null) {
                    Object item = f.get(source);
                    builder.append(item);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
}
