package cs.concurrent;

import java.util.concurrent.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description :
 */
//多线程查找数字数组中的最大值
public class MultiThreadMaxFinder {

    static class FindMaxTask implements Callable<Integer> {
        private int data[];
        private int start;
        private int end;
        FindMaxTask(int[] data,int start,int end){
            this.data = data;
            this.start = start;
            this.end = end;
        }
        public Integer call(){
            int max = Integer.MIN_VALUE;
            for(int i = start;i < end;i++){
                if(data[i] > max) max = data[i];
            }
            return max;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] data = {1,2,5,45,234,667,2,341,99,67,24,66,10,18,44,71,88,222};
        FindMaxTask task1 = new FindMaxTask(data,0,data.length/2);
        FindMaxTask task2 = new FindMaxTask(data,data.length/2,data.length);
        ExecutorService s = Executors.newFixedThreadPool(2);
        Future<Integer> f1 = s.submit(task1);
        Future<Integer> f2 = s.submit(task2);
        System.out.println(Math.max(f1.get(),f2.get()));
        s.shutdown();
    }

}
