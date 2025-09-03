package com.padingpading.spring.hystrix.threadlocal;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 单条数据
 */
public class CommandSemaphore extends HystrixCommand<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String name;
    private final long timeout;

    public CommandSemaphore(String group, String name, long timeout) {
        //指定分组
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("group"))
                //指定key
                .andCommandKey(HystrixCommandKey.Factory.asKey("key"))
                //指定线程池名称
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(" pool"))
                //指定隔离方式
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        //隔离方式
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        //信号量最大通过量。
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(15)))
                ;
        this.name = name;
        this.timeout = timeout;
    }

    // 要封装的业务请求
    @Override
    protected String run() throws Exception {
        logger.info("hystrix command execute");
        if (name == null) {
            throw new RuntimeException("data exception");
        }
        Thread.sleep(timeout); // 休眠
        return "hello " + name;
    }

    // 快速失败的降级逻辑
    @Override
    protected String getFallback() {
        logger.info("return fallback data");
        return "error";
    }
}
