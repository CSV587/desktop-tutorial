package cs.动态代理;

import java.lang.reflect.Proxy;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/27.
 * @Description :
 */
public class ProxyInstanceFactory {

    PersonBean getOwnerProxy(PersonBean p){
        return (PersonBean) Proxy.newProxyInstance(
                p.getClass().getClassLoader(),
                p.getClass().getInterfaces(),
                new OwnerInvocationHandler(p)
        );
    }

    PersonBean getNonOwnerProxy(PersonBean p){
        return (PersonBean) Proxy.newProxyInstance(
                p.getClass().getClassLoader(),
                p.getClass().getInterfaces(),
                new NonOwnerInvocationHandler(p)
        );
    }


}
