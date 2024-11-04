
package com.padingpading.interview.thread.wangwenjun.future.completable_future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author libin
 * @description
 * @date 2023-02-09
 */
public class A_Supply {
    
    public static void testThenAsync() {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
            return "执行第一步";
        }).thenApply(s->{
            //等待处理结果再次处理,再次返回结果。
            System.out.println("执行第二部");
            return "执行第二部";
        });
        try {
            String s = task.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("");
    }
    
    public static void testSupplyAsync() {
        CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
            return "异步执行任务";
        });
        try {
            String s = task.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("");
    }
}
