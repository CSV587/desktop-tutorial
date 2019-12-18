package cs;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description : for循环
 */
public class For {

    public static void main(String[] args) {
        List<String> l = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
        }};
        //会报错，foreach循环中不要使用remove,add操作！观察编译后的class文件，这种方式是通过Iterator迭代器实现for循环的。
        for(String s : l){
            if(s.equals("A")){
                l.remove(s);
            }
        }
        //这种就不会报错了
//        for(int i = 0; i < userNames.size(); i++){
//            if(userNames.get(i).equals("A"))
//                userNames.remove(i);
//        }
        System.out.println(l);
    }

}
