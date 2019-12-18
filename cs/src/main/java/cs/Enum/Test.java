package cs.Enum;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description :
 */
public class Test {

    public static void main(String[] args){

        double a = 2.333;
        double b = 6.666;
        System.out.println(BasicOperation.PLUS.apply(a,b));
        System.out.println(ExtendedOperation.EXP.apply(a,b));

    }

}
