package cs.算法;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : 冒泡排序
 */
public class BubbleSort {

    public static int[] bubbleSort(int[] array) {
        if (array.length == 0)
            return array;
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array.length - 1 - i; j++)
                if (array[j + 1] < array[j]) {
                    int temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
        return array;
    }

    public static void main(String[] args) {
        int[] arr = {3,44,38,5,47,15,36,26,27,2,46,4,19,50,48};
        int[] result = bubbleSort(arr);
        for(int i : result) {
            System.out.print(i+"_");
        }
    }

}
