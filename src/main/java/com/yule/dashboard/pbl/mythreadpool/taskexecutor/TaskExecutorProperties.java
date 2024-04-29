package com.yule.dashboard.pbl.mythreadpool.taskexecutor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "limit.task-executor")
public class TaskExecutorProperties {
    private int corePoolSize;
    private int queueCapacity;
    private int maxPoolSize;
}
