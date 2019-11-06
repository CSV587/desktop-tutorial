package cs.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/27.
 * @Description :
 */
public class NonOwnerInvocationHandler implements InvocationHandler {

    PersonBean person;

    public NonOwnerInvocationHandler(PersonBean person){
        this.person = person;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException{
        try{
            if(method.getName().startsWith("get")){
                return method.invoke(person, args);
            } else if(method.getName().equals("setHotOrNotRating")){
                return method.invoke(person, args);
            } else if(method.getName().startsWith("set")){
                throw new IllegalAccessException();
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
