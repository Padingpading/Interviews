package com.padingpading.spring.hystrix.a_command;


import rx.Observable;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;

public class HystrixObservableCommonTest {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //同步执行
       // syncExecute();
        //异步执行
      //  asyncExecute();
        //订阅立即执行,没有订阅也执行
        //subExecute();
        //有订阅后执行
    //    SubAfterExecute();
    }
    
    private static void SubAfterExecute() throws InterruptedException {
        HystrixCommonDemo hystrixCommonDemo = new HystrixCommonDemo("SubAfterExecute");
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
        HystrixCommonDemo hystrixCommonDemo = new HystrixCommonDemo("SubAfterExecute");
        Observable<String> observe = hystrixCommonDemo.observe();
        Thread.sleep(10000);
        observe.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("通过订阅，获取执行结果：" + s);
            }
        });
    }
    

}
