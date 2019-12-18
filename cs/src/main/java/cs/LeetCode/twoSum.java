package cs.LeetCode;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */
public class twoSum {

    //找出一个数组中相加为target的两个数
    public int[] twoSum(int[] a, int target){
        Map<Integer,Integer> m = new HashMap<>();
        for(int i = 0; i < a.length; ++i){
            int num = target - a[i];
            if(m.containsKey(num)) return new int[]{m.get(num),a[i]};
            m.put(a[i],i);
        }
        throw new RuntimeException("No Answer!");
    }

}
