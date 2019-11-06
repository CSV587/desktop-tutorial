package cs.单例模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
//延迟实例化（lazy instantiaze），用到时才进行实例化
public class Singleton1 {

    private static Singleton1 uniqueInstance;

    private Singleton1(){}

    public static synchronized Singleton1 getInstance() {
        if(uniqueInstance == null) uniqueInstance = new Singleton1();
        return uniqueInstance;
    }

}
