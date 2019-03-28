package com.bgd.app.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @创建人 JackMore
 * @创建时间 2019/3/14
 * @描述
 */
@Configuration
public class AppConfig {
    @Autowired
    Global global;

    /**
     * @描述 配置异步线程池
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //int corePoolSize = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(global.getCorePoolSize());
        executor.setMaxPoolSize(global.getCorePoolSize());
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("bgd:executor:");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * @描述 配置定时器线程池
     * @创建人 JackMore
     * @创建时间 2019/3/22
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        return taskScheduler;
    }
}
