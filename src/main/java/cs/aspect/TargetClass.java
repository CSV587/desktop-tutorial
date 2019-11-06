package cs.aspect;

import org.springframework.stereotype.Component;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/21.
 * @Description :
 */

@Component
public class TargetClass {
    /**
     * 拼接两个字符串
     */
    public String joint(String str1, String str2) {
        System.out.println("目标方法正在执行......");
        return str1 + "+" + str2;
    }
}