package cs.concurrent;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    public static void main(String[] args) {
        fixedThreadPool();
    }

    /**
     * 固定大小的线程池
     *
     * 同时可以处理【参数】个任务，多余的任务会排队，当处理完一个马上就会去接着处理排队中的任务。
     * Callable的任务在后面的blog有更详细的文章说明
     */
    private static void fixedThreadPool(){
        ExecutorService es = Executors.newFixedThreadPool(2);
        //加入5个任务
        for(int i=1 ; i<5; i++){
            final int task = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for(int j=1; j<=2; j++){
                        System.out.println("现在运行的是第【" + task + "】任务");
                        System.out.println(Thread.currentThread().getName() + " is work , now loop to " + j);
                        if(j==2){
                            System.out.println("任务 【" + task + "】运行完成");
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        System.out.println("5个Runnable任务submit完成!!");

        //加入5个Callable任务，该任务执行完后是有返回值的则会发生堵塞，也就是取到5个任务的结果后才会继续往下走
        for(int i=1 ; i<=5; i++){
            final int task = i;
            Future<Integer> future = es.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Callable 任务【" + task + "】运行");
                    return new Random().nextInt(100);
                }
            });

            //如果注释取结果的代码，则不会堵塞
            try {
                System.out.println("任务【" + i + "】返回的结果：" + future.get());
            } catch (Exception  e) {
                e.printStackTrace();
            }
        }
        System.out.println("5个Callable任务submit完成!!" + System.currentTimeMillis() );
        //虽然shutdown方法是等所有任务跑完后才真正停掉线程池，但该方法不会造成堵塞，也就是这代码运行后，下一行代码会立刻运行
        es.shutdown();
        System.out.println("主程序shutdown后退出!!" + System.currentTimeMillis());

        //暴力的直接终止线程池
        //es.shutdownNow();

        //awaitTermination方法是堵塞式的，只有等真的把线程池停掉才会让程序继续往下执行
        try {
            es.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主程序后awaitTermination退出!!" + System.currentTimeMillis());
    }
}