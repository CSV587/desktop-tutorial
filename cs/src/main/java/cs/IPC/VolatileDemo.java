package cs.IPC;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/6.
 * @Description :
 */
public class VolatileDemo {

    private volatile List<Integer> list = new ArrayList<>();
    public static void main(String[] args) {
        VolatileDemo demo =new VolatileDemo();
        new Thread(()->{
            for (int i=0;i<10;i++){
                demo.list.add(i);
                System.out.print(Thread.currentThread().getName());
                System.out.println(demo.list);
            }
        }).start();

        new Thread(()->{
            for (int i=0;i<10;i++){
                demo.list.add(i);
                System.out.print(Thread.currentThread().getName());
                System.out.println(demo.list);
            }
        }).start();
    }

}
