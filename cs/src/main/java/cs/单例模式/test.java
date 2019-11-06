package cs.单例模式;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class test {

    public static void main(String[] arg) throws Exception {

//        MyClass mc1 = MyClass.getInstance();
//        MyClass mc2 = MyClass.getInstance();
//        System.out.println(mc1);
//        System.out.println(mc2);

//        Singleton3 s1 = Singleton3.getInstance();
//        Singleton3 s2 = Singleton3.getInstance();
//        System.out.println(s1);
//        System.out.println(s2);
//        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>();
//        map.put("1","Yes");

//        Singleton4 singleton1 = Singleton4.INSTANCE;
//        Singleton4 singleton2 = Singleton4.INSTANCE;
//        System.out.println("正常情况下，实例化两个实例是否相同："+(singleton1==singleton2));
//        Class cla = Singleton4.class;
//        Constructor c = cla.getDeclaredConstructor(String.class,int.class);
//        c.setAccessible(true);
//        //这里会报“Cannot reflectively create enum objects”异常，因此使用枚举可避免反射攻击
//        //原理：反射在通过newInstance创建对象时，会检查该类是否ENUM修饰，如果是则抛出异常，反射失败（源码有体现）
//        //if ((clazz.getModifiers() & Modifier.ENUM) != 0) throw new IllegalArgumentException("Cannot reflectively create enum objects");
//        Singleton4 singleton = (Singleton4) c.newInstance();
//        System.out.println(singleton.getInstance());

        SortedSet<String> set = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.length() - o2.length();
            }
        });
        set.add("asssda");
        set.add("cssdwwdasd");
        set.add("bfhfy");
        set.add("ofg");
        set.add("yf");
        set.add("ddfasa");
        for(String tmp : set){
            System.out.println(tmp);
        }

//        Map<String,String> m = new LinkedHashMap<>();
//        m.put("wadv","cs");
//        m.put("gnhg","jy");
//        m.put("uyyyy3","love");
//        for(Map.Entry<String,String> entry : m.entrySet()){
//            System.out.println(entry.getKey() + ":" + entry.getValue());
//        }

    }

}
