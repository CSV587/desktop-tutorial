package cs;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : 线程池不要用Executors.newFixedThreadPool()这种方式来整
 */
public class zzz {

    static int len;

    static int[] HeapSort(int[] array) {

        len = array.length;
        if(len < 1) return array;
        buildHeap(array);

        while(len > 0) {
            swap(array, 0, len-1);
            len--;
            alterHeap(array, 0);
        }
        return array;

    }

    static void buildHeap(int[] array) {
        for(int i = len/2-1; i >= 0; i--) {
            alterHeap(array,i);
        }
    }

    static void alterHeap(int[] array, int c) {
        int maxIndex = c;
        if(c * 2 < len && array[maxIndex] < array[c * 2]) maxIndex = c * 2;
        if(c * 2 + 1 < len && array[maxIndex] < array[c * 2 + 1]) maxIndex = c * 2 + 1;
        if(c != maxIndex) {
            swap(array, c, maxIndex);
            alterHeap(array,maxIndex);
        }
    }

    static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static void main(String[] args) {

    }

}
