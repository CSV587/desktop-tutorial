package com.hy.base.bean;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:47 2018/8/30
 * @ Description ：map转换成Bean对象
 * @ Modified By ：
 * @ Version     ：
 */
public final class BeanTools {
    /**
     * .
     * 隐藏构造类
     */
    private BeanTools() {

    }

    /**
     * .
     * Map 转为Bean
     *
     * @param obj Bean 对象
     * @param map Map值
     * @throws InvocationTargetException InvocationTargetException
     * @throws IllegalAccessException    IllegalAccessException
     */
    public static void transMap2Bean(final Object obj,
                                     final Map map)
        throws InvocationTargetException, IllegalAccessException {
        DateTimeConverter dtConverter = new DateTimeConverter();
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.deregister(Date.class);
        convertUtilsBean.register(dtConverter, Date.class);
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean,
            new PropertyUtilsBean());
        beanUtilsBean.populate(obj, map);
    }
}
