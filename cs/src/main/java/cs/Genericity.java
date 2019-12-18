package cs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/14.
 * @Description :
 */

//泛型
public class Genericity {

    class Test1<E> {
        private E[] elements;

        public Test1() {
            //elements = new E[16];//编译时出错，不能创建不可具体化的类型的数组
            elements = (E[]) new Object[16];
        }
    }

    class Test2<E> {
        private Object[] elements;

        public Test2() {
            //elements = new E[16];//编译时出错，不能创建不可具体化的类型的数组
            elements = new Object[16];
        }

        public E test2() {
            return (E) elements[0];
        }
    }

    //此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型
    //在实例化泛型类时，必须指定T的具体类型
    static class Generic<T>{
        //key这个成员变量的类型为T,T的类型由外部指定
        public T key;

        public Generic(T key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
            this.key = key;
        }

        public T getKey(){ //泛型方法getKey的返回值类型为T，T的类型由外部指定
            return key;
        }
    }

    public static void main(String[] args){
//        List<String> stringArrayList = new ArrayList<String>();
//        List<Integer> integerArrayList = new ArrayList<Integer>();
//        Class classStringArrayList = stringArrayList.getClass();
//        Class classIntegerArrayList = integerArrayList.getClass();
//        System.out.println(classStringArrayList.equals(classIntegerArrayList));

//        Generic<Integer> genericInteger = new Generic<Integer>(123456);
//        Generic<String> genericString = new Generic<String>("key_vlaue");
//        System.out.println("key is " + genericInteger.getKey());
//        System.out.println("key is " + genericString.getKey());

//        //不能对确切的泛型类型使用instanceof操作。如下面的操作是非法的，编译时会出错。
//        if(genericString instanceof Generic<String>){//...}


    }

}
