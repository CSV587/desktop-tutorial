package cs.ThreadLocal;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/6.
 * @Description :
 */
public class Test {

    public static void main(String[] args){

        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        AtomicInteger count = new AtomicInteger(0);

        new Thread(() -> {
            int two33 = ConnectionManager.get233();
            System.out.println(two33);
            for(int i = 0; i < 5; i++) {
                synchronized(count){
                    if(count.get() %2 == 1){
                        try {
                            count.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName()+":"+list.get(count.get()));
                    count.incrementAndGet();
                    count.notify();
                }
            }
        }).start();

        new Thread(() -> {
            int two33 = ConnectionManager.get233();
            System.out.println(two33);
            for(int i = 0; i < 5; i++) {
                synchronized (count) {
                    if (count.get() % 2 == 0) {
                        try {
                            count.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(Thread.currentThread().getName()+":"+list.get(count.get()));
                    count.incrementAndGet();
                    count.notify();
                }
            }
        }).start();

    }

}
