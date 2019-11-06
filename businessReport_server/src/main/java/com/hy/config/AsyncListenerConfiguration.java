package com.hy.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/31
 * user: lxg
 * package_name: com.hy.config
 */
@Configuration
@EnableAsync
public class AsyncListenerConfiguration implements AsyncConfigurer {

    /**
     * .
     * 异步线程池监听
     *
     * @return 线程池
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor poolTE = new ThreadPoolTaskExecutor();
        // 设置线程池的数目
        poolTE.setCorePoolSize(10);
        poolTE.setMaxPoolSize(20);
        poolTE.setQueueCapacity(50);
        poolTE.initialize();
        return poolTE;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
