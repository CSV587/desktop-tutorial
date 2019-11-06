package cs.concurrent;

import java.util.concurrent.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/12.
 * @Description :
 */
public class ScheduledExecutorServiceDemo {

    public static void main(String[] arg) throws ExecutionException, InterruptedException {
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(5);
        ScheduledFuture scheduledFuture =
                scheduledExecutorService.schedule(new Callable() {
                                                      public Object call() throws Exception {
                                                          System.out.println("Executed!");
                                                          return "Called!";
                                                      }
                                                  },
                        10,
                        TimeUnit.SECONDS);
        System.out.println("result = " + scheduledFuture.get());
        scheduledExecutorService.shutdown();
    }

}
