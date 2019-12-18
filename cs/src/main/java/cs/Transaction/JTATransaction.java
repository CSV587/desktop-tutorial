package cs.Transaction;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description :
 */
public class JTATransaction {

    public static void main(String[] args){

        List<String> l = new LinkedList<>();
        l.add("a");
        l.add("b");
        l.add("c");
        l.add("");
        l.add("e");
        long count = l.stream().filter(s -> !s.isEmpty()).count();
        System.out.println(count);

    }

}
