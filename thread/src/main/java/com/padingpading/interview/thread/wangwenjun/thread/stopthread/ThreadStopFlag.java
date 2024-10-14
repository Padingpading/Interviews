package com.padingpading.interview.thread.wangwenjun.thread.stopthread;

public class ThreadStopFlag extends Thread {
    
    public void run() {
        try {
            // 让线程休眠10秒
            System.out.println("开始执行");
            Thread.sleep(10000);
            System.out.println("线程执行完毕！");
        } catch (InterruptedException e) {
            System.out.println("线程中断异常！");
        }
    }
    
    public static void main(String[] args) throws IllegalAccessException {
        ThreadStopFlag thread = new ThreadStopFlag();
        thread.start();
        
        // 等待3秒后强制终止线程
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.stop(); // 强制终止线程的执行
        System.out.println("主线程执行完毕！");
    }
}