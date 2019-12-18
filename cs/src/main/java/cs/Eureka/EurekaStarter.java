package cs.Eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/13.
 * @Description :
 */

@EnableDiscoveryClient
@EnableEurekaServer
@SpringBootApplication
public class EurekaStarter {

    public static void main(String[] args) {
        SpringApplication.run(EurekaStarter.class);
    }

}
