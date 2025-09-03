package com.padingpading.spring.hystrix.circuit;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 降级
 */
public class CommandCircuitFailure extends HystrixCommand<String> {
    
    private final String name;

    public CommandCircuitFailure(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                .andCommandPropertiesDefaults(
                        //同时调用fallback的线程数。
                        HystrixCommandProperties.Setter()
                                .withFallbackIsolationSemaphoreMaxConcurrentRequests(15)
//                        设置一个rolling window，滑动窗口中，最少要有多少个请求时，才触发开启短路
//                        举例来说，如果设置为20（默认值），那么在一个10秒的滑动窗口内，如果只有19个请求，即使这19个请求都是异常的，也是不会触发开启短路器的
                        .withCircuitBreakerRequestVolumeThreshold(40)
                        //设置在短路之后，需要在多长时间内直接reject请求，然后在这段时间之后，再重新导holf-open状态，尝试允许请求通过以及自动恢复，默认值是5000毫秒
                        .withCircuitBreakerSleepWindowInMilliseconds(3000)
                        //设置异常请求量的百分比，当异常请求达到这个百分比时，就触发打开短路 器，默认是50，也就是50%
                        .withCircuitBreakerErrorThresholdPercentage(40)
                ));
        this.name = name;
    }
    
    @Override
    protected String run() {
        if(name=="2"){
            //抛出异常
            throw new RuntimeException("this command always fails");
        }
        if(name=="1"){
            return "success";
        }
        return "";
    }
    
    @Override
    protected String getFallback() {
        //返回默认值
        return "Hello Failure " + name + "!";
    }
}
