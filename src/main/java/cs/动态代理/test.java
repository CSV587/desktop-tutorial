package cs.动态代理;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/27.
 * @Description :
 */
//此代理模式套用至具体功能，可以实现不同对象给予不同操作权限的功能
public class test {

    public static void main(String[] args) {
//        System.out.println(args[0]);
//        System.out.println(args[1]);
        ProxyInstanceFactory proxyInstanceFactory1 = new ProxyInstanceFactory();
        ProxyInstanceFactory proxyInstanceFactory2 = new ProxyInstanceFactory();
        PersonBean pp = new Person();
        pp.setName("Bob");
        pp.setGender("Man");
        pp.setInterests("Fucking");
        PersonBean ownerProxy = proxyInstanceFactory1.getOwnerProxy(pp);
        PersonBean nonOwnerProxy = proxyInstanceFactory2.getNonOwnerProxy(pp);
        System.out.println(pp.getName());
        ownerProxy.setName("Jack");
        System.out.println(pp.getName());
        nonOwnerProxy.setName("Rose");
        System.out.println(pp.getName());
    }

}
