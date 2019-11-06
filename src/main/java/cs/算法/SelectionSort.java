package cs.算法;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : 选择排序
 */
public class SelectionSort {

    public static int[] selectionSort(int[] array) {
        if (array.length == 0)
            return array;
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[minIndex]) //找到最小的数
                    minIndex = j; //将最小数的索引保存
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }

    public static void main(String[] args){
        int[] arr = {3,44,38,5,47,15,36,26,27,2,46,4,19,50,48};
        int[] result = selectionSort(arr);
        for(int i : result){
            System.out.print(i+"_");
        }
    }

}
