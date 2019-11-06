package cs;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/18.
 * @Description : 泛型
 */
public class Something {

    public static <A> A coma(A a, A b){
        return a;
    }

    public static void main(String[] args){
        //虽然String是Object的子类，但List<String>并不是List<Object>的子类，所以以下会报错
//        List<Object> a = new ArrayList<String>();
//        a.add(null);
        //如果想让容器的类型具有父子关系，需要使用未知类型，如下：
        List<?> b = new ArrayList<String>();
        b.add(null);
        String[] ss = {"Hello World!"};
        Object[] os = ss;
        System.out.println(os[0]);
        Object[] objects = new Object[10];
        System.out.println(objects[0]);
        objects[0] = new Integer(233);
        System.out.println(objects[0]);
        objects[1] = new String("hhh");
        System.out.println(objects[1]);
        List<String> l = new ArrayList<>();
        System.out.println(l instanceof RandomAccess);
        List<String> ll = new CopyOnWriteArrayList<>();

    }

}
