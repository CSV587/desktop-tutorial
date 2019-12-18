package cs.Zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */

@SpringBootApplication
@EnableZuulProxy //开启Zuul代理
public class ZuulStarter {

    public static void main(String[] args) {

        SpringApplication.run(ZuulStarter.class);

    }

}
