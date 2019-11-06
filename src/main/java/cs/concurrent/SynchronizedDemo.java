package cs.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/22.
 * @Description :
 */
//Java 提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是 JVM 实现的 synchronized，而另一个是 JDK 实现的 ReentrantLock。
//以下是第一种方式
//除非需要使用 ReentrantLock 的高级功能，否则优先使用 synchronized。这是因为 synchronized 是 JVM 实现的一种锁机制，JVM 原生地支持它，
//而 ReentrantLock 不是所有的 JDK 版本都支持。并且使用 synchronized 不用担心没有释放锁而导致死锁问题，因为 JVM 会确保锁的释放。
public class SynchronizedDemo {

//    public synchronized void func1 (){ //它和同步代码块一样，作用于同一个对象。
//    public synchronized static void func1 (){ //作用于整个类
    public void func1() {
//        synchronized (SynchronizedDemo.class) { //作用于整个类，也就是说两个线程调用同一个类的不同对象上的这种同步语句，也会进行同步。
        synchronized (this) { //只作用于同一个对象，如果调用两个对象上的同步代码块，就不会进行同步。
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        }
    }

    public static void main(String[] args) {
//        SynchronizedDemo e1 = new SynchronizedDemo();
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.execute(() -> e1.func1());
//        executorService.execute(() -> e1.func1());
        SynchronizedDemo e1 = new SynchronizedDemo();
        SynchronizedDemo e2 = new SynchronizedDemo();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> e1.func1());
        executorService.execute(() -> e2.func1());
    }
}