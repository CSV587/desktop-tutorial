package cs.单例模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
/*
急切实例化，与延迟实例化相对，Singleton1的延迟实例化通过使用synchronized关键字来保证多线程情况下仍然只有单个实例被创建，但
这样的同步会大大影响程序运行的效率。其实synchronized仅仅是为了防止多条线程“第一次”执行getInstance方法时多创建实例的情况，
第二次就可以直接用if(uniqueInstance == null)的判断来保证单例。因此，可以使用急切实例化，JVM在加载这个类时马上创建此唯一的
单例，保证在任何线程访问uniqueInstance静态变量之前，一定先创建此实例。
*/
public class Singleton2 {

    private static Singleton2 uniqueInstance = new Singleton2();

    private Singleton2(){}

    public static Singleton2 getInstance(){
        return uniqueInstance;
    }

}
