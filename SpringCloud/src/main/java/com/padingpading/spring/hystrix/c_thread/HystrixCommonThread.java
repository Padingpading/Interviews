package com.padingpading.spring.hystrix.c_thread;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class HystrixCommonThread  extends HystrixCommand<String> {
    
    private String name;
    
    public HystrixCommonThread(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixCommonThreadGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("HystrixCommonThreadKey"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(100000))
                //隔离策略
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)));
        this.name = name;
    }
    
    @Override
    protected String run() throws Exception {
        System.out.println("执行中");
        Thread.sleep(5000);
        System.out.println("执行完成");
        return name;
    }
}
