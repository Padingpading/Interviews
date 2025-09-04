package com.padingpading.spring.hystrix.a_command;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class HystrixCommonDemo  extends HystrixCommand<String> {
    
    public static final HystrixCommandKey KEY = HystrixCommandKey.Factory.asKey("HystrixCommonDemo");
    
    private String name;
    
    public HystrixCommonDemo(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixCommonDemo"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(100000)));
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
