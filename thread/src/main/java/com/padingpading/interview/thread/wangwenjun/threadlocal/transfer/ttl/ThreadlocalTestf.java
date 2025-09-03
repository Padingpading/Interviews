package com.padingpading.interview.thread.wangwenjun.threadlocal.transfer.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author libin
 * @description
 * @date 2025/9/1
 */
public class ThreadlocalTestf {
    static TransmittableThreadLocal<User> currentUser = new TransmittableThreadLocal<>();
    static  ExecutorService pool = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(5));
    
    public static void main(String[] args) {
        User user = new User();
        user.setName("123123");
        // 改用TTL包装
        // 线程池需要用TtlExecutors装饰
        currentUser.set(user);
    
        // 提交任务
        pool.execute(() -> {
            System.out.println(currentUser.get()); // 正常获取！
        });
    
    }
    
    @Data
    public static  class User{
        private String name;
        private String age;
    }
}
