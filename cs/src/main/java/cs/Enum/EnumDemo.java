package cs.Enum;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description :
 */

@Slf4j
public class EnumDemo {

    public enum Operation {
        PLUS, MINUS, TIMES, DEVIDE;
    }

    public enum AlamPoints {
        KITCHEN, BATHROOM;
    }

    public interface Command {
        void action();
    }

    public static void main(String[] args) {
//        EnumSet<Operation> enumSet = EnumSet.noneOf(Operation.class);
//        enumSet.add(Operation.DEVIDE);
//        System.out.println(enumSet);
//        enumSet.remove(Operation.DEVIDE);
//        System.out.println(enumSet);
//        enumSet.addAll(Arrays.asList(Operation.values()));
//        System.out.println(enumSet);
//        enumSet.removeAll(Arrays.asList(Operation.MINUS,Operation.PLUS));
//        System.out.println(enumSet);
//        EnumMap enumMap = new EnumMap();
        EnumMap<AlamPoints, Command> em = new EnumMap<>(AlamPoints.class);
        em.put(AlamPoints.KITCHEN, () -> System.out.println("Kitchen fire"));
        em.put(AlamPoints.BATHROOM, () -> System.out.println("Bathroom alert!"));
        for (Map.Entry<AlamPoints, Command> e : em.entrySet()) {
            System.out.print(e.getKey() + ":");
            e.getValue().action();
        }

    }

}
