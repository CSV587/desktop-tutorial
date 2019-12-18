package cs.重载;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description : 慎用重载。本身想要的打印结果是[-3, -2, -1] [-3, -2, -1]，结果运行结果不是。
 * 原因就在于对于List的remove方法有两个，这两个在这里产生了歧义，其中一个是删除下标索引元素，另一个是删除集合中的元素。
 * 如果将代码修改为list.remove((Integer)i)，则可以避免这个问题;
 */
public class ReloadDemo {

    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<Integer>();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = -3; i < 3; i++) {
            set.add(i);
            list.add(i);
        }
        for (int i = 0; i < 3; i++) {
            set.remove(i);
            list.remove(i);
//            list.remove((Integer)i);
        }
        System.out.println(set + " " + list);
    }


}
