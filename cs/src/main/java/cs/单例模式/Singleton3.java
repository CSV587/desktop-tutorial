package cs.单例模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/27.
 * @Description :
 */
//用双重校验锁，在getInstance()中减少使用同步
public class Singleton3 {

    //volatile关键字确保uniqueInstance同一时刻只能被一个线程修改
    private volatile static Singleton3 uniqueInstance;

    private Singleton3(){}

    public static Singleton3 getInstance(){
        if(uniqueInstance == null){
            synchronized (Singleton3.class){
                if(uniqueInstance == null){
                    uniqueInstance = new Singleton3();
                }
            }
        }
        return uniqueInstance;
    }

}
