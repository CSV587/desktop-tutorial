package cs.concurrent.BlockingQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/11.
 * @Description :
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws Exception {
//        BlockingQueue queue = new ArrayBlockingQueue(1024);
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1024);
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(producer).start();
        new Thread(consumer).start();
        Thread.sleep(4000);
        System.out.println("结束！");
    }

}

class Producer implements Runnable{
    protected BlockingQueue queue = null;
    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }
    public void run() {
        try {
            queue.put("1");
            Thread.sleep(1000);
            queue.put("2");
            Thread.sleep(1000);
            queue.put("3");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable{

    protected BlockingQueue queue = null;
    public Consumer(BlockingQueue queue) {
        this.queue = queue;
    }
    public void run() {
        try {
            System.out.println(queue.take());
            System.out.println(queue.take());
            System.out.println(queue.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

