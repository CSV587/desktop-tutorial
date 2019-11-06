package cs.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/22.
 * @Description :
 */
/*
Executor 管理多个异步任务的执行，而无需程序员显式地管理线程的生命周期。这里的异步是指多个任务的执行互不干扰，不需要进行同步操作。
主要有三种 Executor：
CachedThreadPool：一个任务创建一个线程；
FixedThreadPool：所有任务只能使用固定大小的线程；
SingleThreadExecutor：相当于大小为 1 的 FixedThreadPool。
*/
public class ExecutorDemo {

    public static void main(String[] args) {
//        System.out.println(Thread.activeCount());
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 1; i < 10; i++) {
//            RunnableDemo rdd = new RunnableDemo(i);
            executorService.execute(new RunnableDemo(i));
//            Future future = executorService.submit(new Runnable() {
//                public void run() {
//                    System.out.println("Asynchronous task");
//                }
//            });
//            try {
//                System.out.println(future.get()); //returns null if the task has finished correctly.
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.activeCount());
//            System.out.println("..." + i + "...");
        }
//        System.out.println(Thread.activeCount());
        executorService.shutdown();
    }
}
