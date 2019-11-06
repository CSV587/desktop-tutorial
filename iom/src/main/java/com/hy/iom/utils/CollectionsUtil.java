package com.hy.iom.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 11:28 2018/8/20
 * @ Description ：集合常用工具类
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class CollectionsUtil {

    /**
     * @Description 判断集合是否为空
     * @param collection 需要判断的集合
     * @return true为空 false为不空
     * @throws
     */
    public static boolean isEmpty(Collection collection){
        return collection==null || collection.isEmpty();
    }


    /**
     * @Description 判断集合是否不空
     * @param collection 需要判断的集合
     * @return true为不空 false为空
     * @throws
     */
    public static boolean isNotEmpty(Collection collection){
        return !isEmpty(collection);
    }

    /**
     * @Description 判断集合是为空
     * @param map
     * @return  true为空 false为不空
     * @throws
     */
    public static boolean isEmpty(Map map){
        return map==null || map.isEmpty();
    }

    /**
     * @Description 判断集合是否不空
     * @param map 需要判断的集合
     * @return true为不空 false为空
     * @throws
     */
    public static boolean isNotEmpty(Map map){
        return !isEmpty(map);
    }

}
