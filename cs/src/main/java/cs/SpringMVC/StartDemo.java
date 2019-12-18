package cs.SpringMVC;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/11.
 * @Description :
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@EnableWebMvc
public class StartDemo {

    public static void main(String[] args){
        SpringApplication.run(StartDemo.class);
    }

}
