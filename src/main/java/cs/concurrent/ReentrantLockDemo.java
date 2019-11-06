package cs.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/22.
 * @Description :
 */
//Java 提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是 JVM 实现的 synchronized，而另一个是 JDK 实现的 ReentrantLock。
//以下是第二种方式
//除非需要使用 ReentrantLock 的高级功能，否则优先使用 synchronized。这是因为 synchronized 是 JVM 实现的一种锁机制，JVM 原生地支持它，
//而 ReentrantLock 不是所有的 JDK 版本都支持。并且使用 synchronized 不用担心没有释放锁而导致死锁问题，因为 JVM 会确保锁的释放。
public class ReentrantLockDemo implements Runnable{

    public static ReentrantLock lock = new ReentrantLock();

    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 20; j++) {
            lock.lock();
            //支持重入锁
            lock.lock();
            try {
                i++;
            } finally {
                //执行两次解锁
                lock.unlock();
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo tl = new ReentrantLockDemo();
        Thread t1=new Thread(tl);
        Thread t2=new Thread(tl);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //输出结果：20000000
        System.out.println(i);
    }

}