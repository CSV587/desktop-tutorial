package cs.算法;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : 希尔排序
 */
public class ShellSort {

    public static int[] ShellSort(int[] array) {
        int len = array.length;
        int temp, gap = len / 2;
        while (gap > 0) {
            for (int i = gap; i < len; i++) {
                temp = array[i];
                int preIndex = i - gap;
                while (preIndex >= 0 && array[preIndex] > temp) {
                    array[preIndex + gap] = array[preIndex];
                    preIndex -= gap;
                }
                array[preIndex + gap] = temp;
            }
            gap /= 2;
        }
        return array;
    }

    public static void main(String[] args){
        int[] arr = {3,44,38,5,47,15,36,26,27,2,46,4,19,50,48};
        int[] result = ShellSort(arr);
        for(int i : result){
            System.out.print(i+"_");
        }
    }

}
