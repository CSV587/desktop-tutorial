package com.hy.util;

import javax.xml.bind.Marshaller;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-11
 * user: lxg
 * package_name: com.hy.util
 */
public class MarshallerListener extends Marshaller.Listener {

    /**
     * .
     * 数据格式化
     *
     * @param source 原始对象
     */
    @Override
    public void beforeMarshal(final Object source) {
        super.beforeMarshal(source);
        List<Field> fields = new ArrayList<>();
        Class tmpClass = source.getClass();
        while (tmpClass != null) {
            fields.addAll(Arrays.asList(tmpClass.getDeclaredFields()));
            tmpClass = tmpClass.getSuperclass();
        }
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                if (f.get(source) == null) {
                    if (f.getType() == String.class) {
                        f.set(source, "");
                    } else if (f.getType() != List.class
                        && f.getType() != Map.class) {
                        Class model;
                        if (f.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType pt
                                = (ParameterizedType) f.getGenericType();
                            model = (Class) pt.getActualTypeArguments()[0];
                        } else {
                            model = f.getType();
                        }
                        f.set(source, model.newInstance());
                    }
                }
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}
