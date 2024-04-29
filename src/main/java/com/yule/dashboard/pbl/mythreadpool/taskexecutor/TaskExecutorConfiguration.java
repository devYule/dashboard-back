package com.yule.dashboard.pbl.mythreadpool.taskexecutor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class TaskExecutorConfiguration {

    private final TaskExecutorProperties properties;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor task = new ThreadPoolTaskExecutor();
        task.setCorePoolSize(properties.getCorePoolSize());
        task.setQueueCapacity(properties.getQueueCapacity());
        task.setMaxPoolSize(properties.getMaxPoolSize());
        task.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return task;
    }
}
