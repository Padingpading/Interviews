package com.padingpading.interview.thread.wangwenjun.thread.sleep;

/**
 * @author libin
 * @description
 * @date 2024/10/10
 */
public class Sleep  implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            System.out.println("子线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args) {
        Sleep sleep = new Sleep();
        Thread thread = new Thread(sleep);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程执行");
        
    }
}