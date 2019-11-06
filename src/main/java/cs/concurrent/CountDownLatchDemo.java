package cs.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/11.
 * @Description :
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        Waiter waiter = new Waiter(latch);
        Decrementer decrementer = new Decrementer(latch);
        new Thread(waiter).start();
        new Thread(decrementer).start();
        Thread.sleep(4000);
    }

}

class Waiter implements Runnable{
    CountDownLatch latch = null;
    public Waiter(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            latch.await();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Waiter Released");
    }
}
class Decrementer implements Runnable {
    CountDownLatch latch = null;
    public Decrementer(CountDownLatch latch) {
        this.latch = latch;
    }
    public void run() {
        try {
            Thread.sleep(1000);
            this.latch.countDown();
            System.out.println("latch - 1");
            Thread.sleep(1000);
            this.latch.countDown();
            System.out.println("latch - 1");
            Thread.sleep(1000);
            this.latch.countDown();
            System.out.println("latch - 1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
