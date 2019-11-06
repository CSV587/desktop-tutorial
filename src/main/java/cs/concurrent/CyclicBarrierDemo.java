package cs.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/12.
 * @Description :
 */
public class CyclicBarrierDemo {
    public static void main(String[] arg){
        Runnable barrier1Action = () -> System.out.println("BarrierAction 1 executed ");
        Runnable barrier2Action = () -> System.out.println("BarrierAction 2 executed ");
        CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
        CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);
        CyclicBarrierRunnable barrierRunnable1 =
                new CyclicBarrierRunnable(barrier1, barrier2);
        CyclicBarrierRunnable barrierRunnable2 =
                new CyclicBarrierRunnable(barrier1, barrier2);
        new Thread(barrierRunnable1).start();
        new Thread(barrierRunnable2).start();
    }
}

class CyclicBarrierRunnable implements Runnable{
    CyclicBarrier barrier1;
    CyclicBarrier barrier2;
    public CyclicBarrierRunnable(
            CyclicBarrier barrier1,
            CyclicBarrier barrier2) {
        this.barrier1 = barrier1;
        this.barrier2 = barrier2;
    }
    public void run() {
        try {
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() +
                    " waiting at barrier 1");
            this.barrier1.await();
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() +
                    " waiting at barrier 2");
            this.barrier2.await();
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() +
                    " done!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
