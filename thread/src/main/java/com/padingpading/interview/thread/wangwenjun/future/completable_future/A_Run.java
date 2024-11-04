

package com.padingpading.interview.thread.wangwenjun.future.completable_future;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author libin
 * @description
 * @date 2023-02-09
 */
public class A_Run {
    
    
    public static void main(String[] args) throws IOException {
        CompletableFuture<Void> future =
                CompletableFuture.runAsync(() -> {
            System.out.println("1111");
        });
        future.thenRun(()->{System.out.println("2222");});
        future.thenRun(()->{System.out.println("3333");});
        future.thenRun(()->{System.out.println("4444");});
        future.thenRun(()->{System.out.println("5555");});
        System.in.read();
    }
}
