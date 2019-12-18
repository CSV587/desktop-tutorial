package cs.ScheduleJob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/11.
 * @Description :
 */

@Component
public class ScheduleJobDemo {

    @Scheduled(cron = "0/10 * * * * ?") //每隔10秒执行一次
    public void job1() {
        System.out.println("执行任务job1:" + new Date());
    }

    @Scheduled(fixedRate = 5000) //单位ms,上次开始执行时间点后5秒再次执行
    public void job2() {
        System.out.println("执行任务job2:" + new Date());
    }

    @Scheduled(fixedDelay = 3000) //单位ms,上次执行完毕时间点后3秒再次执行
    public void job3() {
        System.out.println("执行任务job3:" + new Date());
    }

}
