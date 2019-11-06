package cs.concurrent;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/16.
 * @Description :
 */
public class AtomicIntegerDemo {

    private static final int THREADS_CONUT = 20;
    public static AtomicInteger count = new AtomicInteger(0);
    public static void increase() {
        count.incrementAndGet();
    }

//    public static int count = 0;
//    public static void increase(){count++;}

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_CONUT];
        for (int i = 0; i < THREADS_CONUT; i++) {
            threads[i] = new Thread(() -> {
                for (int i1 = 0; i1 < 100000; i1++) {
                    increase();
                }
            });
            threads[i].start();
        }

        while (Thread.activeCount() > 2) {
            Thread.currentThread().getThreadGroup().list();
            Thread.yield();
        }
        System.err.println(count);
    }

}