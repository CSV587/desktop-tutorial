package cs.concurrent.BlockingQueue;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/11.
 * @Description :
 */
public class BlockingDequeDemo {

    public static void main(String[] args) throws Exception {
        BlockingDeque<String> deque = new LinkedBlockingDeque<>();
        deque.addFirst("1");
        deque.addLast("2");
        System.out.println(deque.takeLast());
        System.out.println(deque.take());
    }

}
