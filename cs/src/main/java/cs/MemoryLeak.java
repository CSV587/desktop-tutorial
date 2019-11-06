package cs;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/14.
 * @Description :
 */
public class MemoryLeak {

    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public MemoryLeak(){
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object o){
        ensureCapacity();
        elements[size++] = o;
    }

    public Object pop(){
        //这样写会导致内存泄漏
//        if(size == 0)
//            throw new EmptyStackException();
//        return elements[--size];

        //应该取出后及时清空
        if(size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null;
        return result;
    }

    private void ensureCapacity(){
        if(elements.length == size){
            elements = Arrays.copyOf(elements, 2*size+1);
        }
    }

}
