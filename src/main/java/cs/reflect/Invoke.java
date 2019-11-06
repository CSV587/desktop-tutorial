package cs.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/14.
 * @Description :
 */
public class Invoke {

    public static void main(String[] args) throws Exception {
        //通过反射创建私有构造器
        Class cc = PrivilegeManagement.class;
        Constructor constructor = cc.getDeclaredConstructor();
        constructor.setAccessible(true);
        PrivilegeManagement p = (PrivilegeManagement) constructor.newInstance();
        //通过反射调用类中的private方法
        Method m = cc.getDeclaredMethod("changeC",new Class[]{int.class});
        m.setAccessible(true);
        m.invoke(p,233);
    }

}
