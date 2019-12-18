package cs.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
以下是第2种方式
*/
public class CallableDemo implements Callable<Integer> { //与 Runnable 相比，Callable 可以有返回值，返回值通过 FutureTask 进行封装。

    public Integer call() {
        return 123;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableDemo callableDemo = new CallableDemo();
        FutureTask<Integer> ft = new FutureTask<>(callableDemo);
        Thread thread = new Thread(ft);
        thread.start();
        System.out.println(ft.get());
    }

}

/*
对于三种方式，总的来说实现接口会更好一些，因为：
        Java 不支持多重继承，因此继承了 Thread 类就无法继承其它类，但是可以实现多个接口；
        类可能只要求可执行就行，继承整个 Thread 类开销过大。
*/
