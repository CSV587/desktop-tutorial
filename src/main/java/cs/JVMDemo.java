package cs;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/12.
 * @Description :
 */
public class JVMDemo {
    public static void main(String[] args) {
        long a1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            alloc();
        }
        // 查看执行时间
        long a2 = System.currentTimeMillis();
        System.out.println("cost " + (a2 - a1) + " ms");
        System.gc();
//        while(true){
//
//        }
        // 为了方便查看堆内存中对象个数，线程sleep
//        try {
//            Thread.sleep(120000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
    }

    private static void alloc() {
        User user = new User();
    }

    static class User {

    }

}
