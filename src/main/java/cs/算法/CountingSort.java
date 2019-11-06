package cs.算法;

import java.util.Arrays;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : 计数排序
 */
public class CountingSort {

    /**
     * 计数排序
     *
     * @param array
     * @return
     */
    public static int[] CountingSort(int[] array) {
        if (array.length == 0) return array;
        int bias, min = array[0], max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max)
                max = array[i];
            if (array[i] < min)
                min = array[i];
        }
        bias = 0 - min;
        int[] bucket = new int[max - min + 1];
        Arrays.fill(bucket, 0);
        for (int i = 0; i < array.length; i++) {
            bucket[array[i] + bias]++;
        }
        int index = 0, i = 0;
        while (index < array.length) {
            if (bucket[i] != 0) {
                array[index] = i - bias;
                bucket[i]--;
                index++;
            } else
                i++;
        }
        return array;
    }

    public static void main(String[] args){
        int[] arr = {3,44,38,5,47,15,36,26,27,2,46,4,19,50,48};
        int[] result = CountingSort(arr);
        for(int i : result){
            System.out.print(i+"_");
        }
    }

}
