package cs;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.lang.Character.isDigit;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description :
 */
public class LambdaDemo {

    public static void main(String[] args) {

//        List<String> l = new LinkedList<>();
//        l.add("a");
//        l.add("b");
//        l.add("c");
//        l.add("");
//        l.add("e");
//        //使用filer和collect搜集符合条件的值转成新的list
//        List<String> l1 = l.stream().filter(s -> {
//            System.out.print(s);
//            return !s.isEmpty();
//        }).collect(Collectors.toList());
//        System.out.println(l1);
//        //对集合中满足条件的值进行计数
//        long count = l.stream().filter(s -> {
//            System.out.print(s);
//            return !s.isEmpty();
//        }).count();
//        System.out.println(count);
//        //使用map函数将一种类型的值转化为另一种类型
//        List<String> l2 = l.stream().map(s -> s.toUpperCase()).collect(Collectors.toList());
//        //Stream.of
//        List<String> l3 = Stream.of("a","b").map(s -> {
//            System.out.print(s.toUpperCase());
//            return s;
//        }).collect(Collectors.toList());
//        //filer中可以执行方法，return的true或false作为是否过滤的依据
//        List<String> ll = Stream.of("a","2b").filter(s -> {
//            if(isDigit(s.charAt(0))) System.out.println(s);
//            return true;
//        }).collect(Collectors.toList());
//        System.out.println(ll);
//        //前面已介绍过 map 操作，它可用一个新的值代替 Stream 中的值。但有时，用户希望让 map 操作有点变化，
//        //生成一个新的 Stream 对象取而代之。用户通常不希望结果是一连串的流，此时 flatMap 最能派上用场。
//        List<Integer> l4 = Stream.of(Arrays.asList(1,2),Arrays.asList(3,4))
//                .flatMap(number -> number.stream()).collect(Collectors.toList());
//        System.out.println(l4);
//        //max和min取最值
//        int max = Stream.of(new Integer(1),new Integer(3),new Integer(2))
//                .max(Comparator.comparing(i -> i)).get();
//        int min = Stream.of(new tt("a",1),new tt("b",3),new tt("c",2))
//                .min(Comparator.comparing(t -> t.getAge())).get().getAge();
//        System.out.println(max+":"+min);
//        //使用reduce求和，第一个参数为初值，然后对其进行数组元素值的累加
//        int count = Stream.of(1,2,3,4).reduce(0,(acc, e) -> acc + e);
//        System.out.println(count);

        List<Integer> l = Arrays.asList(5,3,1,7,6,22,15,11,85,93,110,10,40);
        List<String> l2 = Arrays.asList("a","b","c","1","2","3");
        //流式排序
        List<Integer> ll = l.stream().sorted((o1, o2) -> o2 - o1).collect(Collectors.toList());
        //反转链表
        Collections.reverse(ll);
        //求平均值
        Double lll = l.stream().collect(Collectors.averagingInt(c -> c));
        //求最大值
        Optional max = l.stream().collect(Collectors.maxBy(Comparator.comparing(c -> c)));
        //分组
        Map<Integer,List<Integer>> map = l.stream().collect(Collectors.groupingBy(LambdaDemo::isSmallerThan50));
        //分块，与分组的区别在于生成的Map的key只能是boolean类型（因为参数的类型不一样）
        Map<Boolean,List<Integer>> map2 = l.stream().collect(Collectors.partitioningBy(LambdaDemo::isBiggerThan50));
        //joining生成新字符串
        String ds = l2.stream().collect(Collectors.joining(",","(",")"));
        System.out.println(ds);
        //新的for循环
        map2.forEach((flag,c) -> System.out.println(flag+":"+c));
        l.forEach((e) -> System.out.print(e+"--"));

    }

    public static boolean isBiggerThan50(int i){
        if(i >= 50) return true;
        else return false;
    }

    public static int isSmallerThan50(int i){
        if(i <= 50) return 1;
        else return 0;
    }


    static class tt {

        String name;
        int age;
        public tt(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }

    }

}
