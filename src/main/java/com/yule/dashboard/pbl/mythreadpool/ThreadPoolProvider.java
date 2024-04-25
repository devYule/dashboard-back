package com.yule.dashboard.pbl.mythreadpool;

import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThreadPoolProvider {

    private final int threadPoolSize;
    @Getter
    private final ExecutorService threadPool;

    public ThreadPoolProvider(@Value("${limit.thread-pool}") int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);

    }

}
