package com.padingpading.interview.thread.wangwenjun.deadlock;


import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock:死锁
 */
public class DeadLockForReentrantLock {
    private static final ReentrantLock lock1=new ReentrantLock();
    private static final ReentrantLock lock2=new ReentrantLock();
    
    public static void main(String[] args) {
        new Thread(()->{
            lock1.lock();
            try{
                Thread.sleep(3000);
                lock2.lock();
                System.out.println("Thread1");
                lock2.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock1.unlock();
        },"Thread1").start();
        
        new Thread(()->{
            lock2.lock();
            try{
                Thread.sleep(3000);
                lock1.lock();
                System.out.println("Thread2");
                lock1.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock2.unlock();
        },"Thread2").start();
    }
}