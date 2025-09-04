
package com.padingpading.spring.hystrix.b_command_name;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Hystrix的名称
 */
public class HystrixCommonName extends HystrixCommand<String> {
    private String name;
    
    public HystrixCommonName(String name) {
        //一个Command必须有group
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HystrixCommonDemoGroup"))
        //设置common-key
                .andCommandKey(    @Override
                        HystrixCommandKey.Factory.asKey("HystrixCommonDemoKey"))
        //设置threadPoolKey,没有的情况下使用group作为 threadPoolKey
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("HystrixCommonDemoThreadPool")));
        this.name = name;
    }
    protected String run() throws Exception {
        System.out.println("执行中");
        Thread.sleep(5000);
        System.out.println("执行完成");
        return name;
    }
}
