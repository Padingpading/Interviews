package com.padingpading.transaction.akc.autoconfig;


import com.padingpading.transaction.akc.executor.InsurableTaskExecutor;
import com.padingpading.transaction.akc.executor.ReversibleTaskExecutor;
import com.padingpading.transaction.akc.executor.impl.InsurableTaskExecutorImpl;
import com.padingpading.transaction.akc.executor.impl.ReversibleTaskExecutorImpl;
import com.padingpading.transaction.akc.executor.impl.TransactionTaskManager;
import com.padingpading.transaction.akc.job.InsurableTaskExceptionRecoverJob;
import com.padingpading.transaction.akc.job.LogCleanJob;
import com.padingpading.transaction.akc.job.ReversibleTaskExceptionRecoverJob;
import com.padingpading.transaction.akc.mapper.TransactionTaskLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * 事务框架starter配置
 *
 * @author liuhaoyong
 * @date 2019年6月19日 上午11:50:15
 */
@Configuration
@EnableConfigurationProperties(TransactionTaskProperties.class)
@ConditionalOnProperty(name = "com.mengxiang.transaction.task.enable", havingValue = "true")
public class TransactionTaskAutoConfiguration {
    
    @Autowired
    private TransactionTaskProperties taskExecutorProperties;
    
    @Autowired
    private TransactionTaskLogMapper transactionLogMapper;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @Bean
    @ConditionalOnMissingBean(TransactionTaskManager.class)
    public TransactionTaskManager transactionTaskManager() {
        return new TransactionTaskManager(transactionLogMapper);
    }
    
    @Bean
    @ConditionalOnMissingBean(InsurableTaskExecutor.class)
    public InsurableTaskExecutor insurableTaskExecutor(TransactionTaskManager manager) {
        return new InsurableTaskExecutorImpl(manager, transactionLogMapper,
                this.buildInsurableThradPool(taskExecutorProperties),
                this.buildAcceptThreadPool(taskExecutorProperties), eventPublisher, taskExecutorProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean(ReversibleTaskExecutor.class)
    public ReversibleTaskExecutor reversibleTaskExecutor(TransactionTaskManager manager) {
        return new ReversibleTaskExecutorImpl(manager, transactionLogMapper,
                this.buildReversalExecutor(taskExecutorProperties), taskExecutorProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean(InsurableTaskExceptionRecoverJob.class)
    public InsurableTaskExceptionRecoverJob inserableExceptionRecoverJob() {
        return new InsurableTaskExceptionRecoverJob(transactionLogMapper, taskExecutorProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean(ReversibleTaskExceptionRecoverJob.class)
    public ReversibleTaskExceptionRecoverJob reversibleExceptionRecoverJob() {
        return new ReversibleTaskExceptionRecoverJob(transactionLogMapper, taskExecutorProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean(LogCleanJob.class)
    public LogCleanJob deleteExpireTaskJob() {
        return new LogCleanJob(transactionLogMapper, taskExecutorProperties);
    }
    
    private ThreadPoolTaskExecutor buildReversalExecutor(TransactionTaskProperties executorProperties) {
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(executorProperties.getReversalCorePoolSize());
        // 设置最大线程数
        executor.setMaxPoolSize(executorProperties.getReversalMaxPoolSize());
        // 设置队列大小
        executor.setQueueCapacity(executorProperties.getReversalQueueCapacity());
        // 设置线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(60 * 5);
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("reversal-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 执行初始化
        executor.initialize();
        
        return executor;
    }
    
    private ThreadPoolTaskExecutor buildInsurableThradPool(TransactionTaskProperties executorProperties) {
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(executorProperties.getInsureCorePoolSize());
        // 设置最大线程数
        executor.setMaxPoolSize(executorProperties.getInsureMaxPoolSize());
        // 设置队列大小
        executor.setQueueCapacity(executorProperties.getInsureQueueCapacity());
        // 设置线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(60 * 5);
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("insure-task-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 执行初始化
        executor.initialize();
        
        return executor;
    }
    
    private ThreadPoolTaskExecutor buildAcceptThreadPool(TransactionTaskProperties executorProperties) {
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(executorProperties.getAcceptCorePoolSize());
        // 设置最大线程数
        executor.setMaxPoolSize(executorProperties.getAcceptMaxPoolSize());
        // 设置队列大小
        executor.setQueueCapacity(-1);
        
        // 设置线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(60 * 5);
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("accept-thread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 执行初始化
        executor.initialize();
        
        return executor;
    }
    
}
