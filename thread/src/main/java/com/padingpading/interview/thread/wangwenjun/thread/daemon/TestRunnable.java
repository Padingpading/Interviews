package com.padingpading.interview.thread.wangwenjun.thread.daemon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author libin
 * @description
 * @date 2024/10/10
 */
class TestRunnable implements Runnable {
    
    public void run() {
        try {
            System.out.println("关闭");
            Thread.sleep(1000); // 守护线程阻塞1秒后运行
            File f = new File("daemon.txt");
            FileOutputStream os = new FileOutputStream(f, true);
            os.write("daemon".getBytes());
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Runnable tr = new TestRunnable();
        Thread thread = new Thread(tr);
        thread.setDaemon(true); // 设置守护线程（必须在thread.start()之前）
        thread.start(); // 开始执行分进程
        Thread.sleep(3000);
    }
}
