package edu.shmonin.bookshelftest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

    @Value("${springAsyncConfig.corePoolSize}")
    private int corePoolSize;
    @Value("${springAsyncConfig.maxPoolSize}")
    private int maxPoolSize;
    @Value("${springAsyncConfig.queueCapacity}")
    private int queueCapacity;
    @Value("${springAsyncConfig.threadNamePrefix}")
    private String threadNamePrefix;
    @Value("${springAsyncConfig.waitForTasksToCompleteOnShutdown}")
    private boolean waitForTasksToCompleteOnShutdown;

    @Override
    public Executor getAsyncExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        executor.initialize();
        return executor;
    }
}