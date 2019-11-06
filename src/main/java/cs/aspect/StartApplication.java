package cs.aspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/21.
 * @Description :
 */

@EnableAutoConfiguration
@SpringBootApplication
public class StartApplication {

    public static void main(final String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(StartApplication.class, args);

        TargetClass targetClass = applicationContext.getBean(TargetClass.class);

        String result = targetClass.joint("spring","aop");

        System.out.println("result:" + result);

    }

}
