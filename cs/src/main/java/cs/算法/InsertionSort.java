package cs.算法;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : 插入排序
 */
public class InsertionSort {

    public static int[] insertionSort(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    public static void main(String[] args){
        int[] arr = {3,44,38,5,47,15,36,26,27,2,46,4,19,50,48};
        int[] result = insertionSort(arr);
        for(int i : result){
            System.out.print(i+"_");
        }
    }

}
