package cs.单例模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/14.
 * @Description :
 */
//枚举是实现单例模式的最好办法，可避免反射攻击和序列化问题
public enum Singleton4 {

    INSTANCE;

    private Singleton4(){}

    public Singleton4 getInstance(){
        return INSTANCE;
    }

}
