package com.hy.iom.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hy.iom.entities.CallContentMatchResult;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanTools {

    /**
     *
     *
     * Map转换层Bean，使用泛型免去了类型转换的麻烦。
     * @param <T>
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        T bean = null;
        try {
            bean = clazz.newInstance();
            BeanUtils.populate(bean, mapConvert(map,clazz));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return bean;
    }

    private static <T> Map<String,Object> mapConvert(Map<String, Object> map,Class<T> clazz){
        List<Field> field = getAllField(clazz);
        Map<String, Object> res = new HashMap<>();
        for(String key : map.keySet()){
            Object value = map.get(key);
            for(int i=0;i<field.size();i++){
                if(key.equalsIgnoreCase(field.get(i).getName())){
                    res.put(field.get(i).getName(),value);
                    break;
                }
            }
        }
        return res;
    }

    private static List<Field> getAllField(Class clazz){
        List<Field> fieldList = new ArrayList<>() ;
        while (clazz != null && !clazz.getName().toLowerCase().equals("java.lang.object")) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(clazz .getDeclaredFields()));
            clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

    public static <T> List<T> jsonArray2BeanList(String arrayStr, Class<T> clazz) {
        List<T> list = new LinkedList<>();
        JSONArray jsonArray = JSON.parseArray(arrayStr);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            T t = JSONObject.toJavaObject(jsonObject,clazz);
            list.add(t);
        }
        return list;
    }
}
