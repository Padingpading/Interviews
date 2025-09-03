package com.padingpading.spring.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 单条数据
 */
public class CommandHello extends HystrixCommand<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String name;
    private final long timeout;

    public CommandHello(String group, String name, long timeout) {
        // 指定命令的分組，同一组使用同一个线程池
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(group)));
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
