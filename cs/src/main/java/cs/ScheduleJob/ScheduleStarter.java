package cs.ScheduleJob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/11.
 * @Description :
 */
@SpringBootApplication
@EnableScheduling //通过此注解开启定时任务功能
public class ScheduleStarter {

    public static void main(String[] args){
        SpringApplication.run(ScheduleStarter.class);
    }

}
