package cs.单例模式;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/26.
 * @Description :
 */
public class MyClass {

    private MyClass(){}

    public static MyClass getInstance(){
        return new MyClass();
    }

}
