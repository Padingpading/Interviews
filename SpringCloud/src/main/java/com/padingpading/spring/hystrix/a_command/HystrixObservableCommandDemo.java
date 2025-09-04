package com.padingpading.spring.hystrix.a_command;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * 执行多条。
 */
public class HystrixObservableCommandDemo extends HystrixObservableCommand<String> {
    private String[] names;
    
    public HystrixObservableCommandDemo(String[] names) {
        super(HystrixCommandGroupKey.Factory.asKey("HystrixObservableCommandDemo"));
        this.names = names;
    }
    
    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            
            @Override
            public void call(Subscriber<? super String> observer) {
                try {
                    for(String name : names) {
                        observer.onNext(name);
                    }
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
