package com.padingpading.spring.hystrix.a_command;


import rx.Observable;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;

public class HystrixCommonTest {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //订阅立即执行,没有订阅也执行
       // subExecute();
        //有订阅之后才会执行
        SubAfterExecute();
    }
    private static void SubAfterExecute() throws InterruptedException {
        String[] names = {"1", "2", "3"};
        HystrixObservableCommandDemo hystrixCommonDemo = new HystrixObservableCommandDemo(names);
            Observable<String> stringObservable = hystrixCommonDemo.toObservable();
            Thread.sleep(5000);
        stringObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("通过订阅，获取执行结果：" + s);
            }
        });
        Thread.sleep(10000);
    }
    
    private static void subExecute() throws InterruptedException {
        String[] names = {"1", "2", "3"};
        HystrixObservableCommandDemo hystrixObservableCommandDemo = new HystrixObservableCommandDemo(names);
        Observable<String> observe = hystrixObservableCommandDemo.observe();
        Thread.sleep(10000);
        observe.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("通过订阅，获取执行结果：" + s);
            }
        });
        Thread.sleep(30000);
    }
}
