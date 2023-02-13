//package com.padingpading.interview.thread.wangwenjun.future.completable_future;
//
//import java.text.SimpleDateFormat;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Date;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author libin
// * @description
// * @date 2023-02-09
// */
//public class A_create {
//    private static  Executor executor = Executors.newCachedThreadPool();
//        //返回时间和当前线程信息的字符串
//        static String getTimeAndThreadInfo() {
//            return DateTimeFormatter.ISO_TIME.format(LocalTime.now()) + " " + Thread.currentThread().getName();
//        }
//        //休眠指定毫秒，忽略IterruptedException
//        static void sleep(int ms) {
//            try {
//                TimeUnit.MILLISECONDS.sleep(ms);
//            } catch (InterruptedException e) {
//            }
//        }
//
//        public static void main(String[] args) {
//            CompletableFuture<String> base = new CompletableFuture<>();
//            CompletableFuture<String> future = base.thenApply(s -> {
//                return s + " 2";
//            });
//            base.thenAccept(s -> log.info(s + "3-1")).thenAccept(aVoid -> log.info("3-2"));
//            base.thenAccept(s -> log.info(s + "4-1")).thenAccept(aVoid -> log.info("4-2"));
//            base.complete("1");
//            log.info("base result: {}", base.get());
//            log.info("future result: {}", future.get());
//        }
//
//}
