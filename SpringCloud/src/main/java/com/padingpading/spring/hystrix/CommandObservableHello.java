package com.padingpading.spring.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;

/**
 * @author libin
 * @description
 * @date 2022-03-13
 */
public class CommandObservableHello extends HystrixObservableCommand<String> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public  CommandObservableHello(String group) {
        // 指定命令的分組，同一组使用同一个线程池
        super(HystrixCommandGroupKey.Factory.asKey(group));
    }

    @Override
    protected Observable<String> construct() {
        logger.info("hystrix command execute");
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                // 发送多条数据
                subscriber.onNext("hello world");
                subscriber.onNext("hello hystrix");
                subscriber.onNext("hello command");
                subscriber.onCompleted();
            }
        });
    }

    // 快速失败的降级逻辑
    @Override
    protected Observable<String> resumeWithFallback() {
        logger.info("return fallback data");
        return Observable.just("error");
    }
}
