package cs;

import java.util.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/27.
 * @Description :
 */
public class TreeMapDemo {

    public static void main(String[] args) {

//        Map<String, String> map = new TreeMap<String, String>(
//                new Comparator<String>() {
//                    public int compare(String obj1, String obj2) {
//                        // 降序排序
//                        return obj2.compareTo(obj1);
//                    }
//                });
//        map.put("c", "ccccc");
//        map.put("a", "aaaaa");
//        map.put("b", "bbbbb");
//        map.put("d", "ddddd");
//        Set<String> keySet = map.keySet();
//        Iterator<String> iter = keySet.iterator();
//        while (iter.hasNext()) {
//            String key = iter.next();
//            System.out.println(key + ":" + map.get(key));
//        }

        Map<String, String> map = new TreeMap<String, String>();
        map.put("d", "ddddd");
        map.put("b", "bbbbb");
        map.put("a", "aaaaa");
        map.put("c", "ccccc");
        map.put("d", "ddddd");
        //这里将map.entrySet()转换成list
        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }

        });
        for(Map.Entry<String,String> mapping:list){
            System.out.println(mapping.getKey()+":"+mapping.getValue());
        }

    }

}
