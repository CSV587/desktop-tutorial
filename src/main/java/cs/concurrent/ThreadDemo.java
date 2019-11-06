package cs.concurrent;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/22.
 * @Description :
 */
/*
有三种使用线程的方法：
    1.实现 Runnable 接口；
    2.实现 Callable 接口；
    3.继承 Thread 类。
实现 Runnable 和 Callable 接口的类只能当做一个可以在线程中运行的任务，不是真正意义上的线程，因此最后还需要通过 Thread 来调用。可以说任务是通过线程驱动从而执行的。
以下是第3种方式
*/
public class ThreadDemo extends Thread {
//同样也是需要实现 run() 方法，因为 Thread 类也实现了 Runable 接口。
//当调用 start() 方法启动一个线程时，虚拟机会将该线程放入就绪队列中等待被调度，当一个线程被调度时会执行该线程的 run() 方法。

    public void run(){
        System.out.println("线程跑起来！");
    }

    public static void main(String[] args) {
        ThreadDemo mt = new ThreadDemo();
        mt.start(); }
}

/*
对于三种方式，总的来说实现接口会更好一些，因为：
        Java 不支持多重继承，因此继承了 Thread 类就无法继承其它类，但是可以实现多个接口；
        类可能只要求可执行就行，继承整个 Thread 类开销过大。
*/
