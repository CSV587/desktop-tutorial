package cs.重载;

import java.util.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description : 令人疑惑的重载示例，重载方法的选择是静态的，而对于重写方法的选择则是动态的。
 */
public class CollectionClassifier {

    public static String classify(Set<?> set) {
        return "Set";
    }

    public static String classify(List<?> list) {
        return "List";
    }

    public static String classify(Collection<?> collection) {
        return "Unknown Collection";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {new HashSet<String>(), new ArrayList<String>(), new HashMap<String, String>().values()};
        for (Collection<?> c : collections) {
            System.out.println(classify(c));
        }
    }


}
