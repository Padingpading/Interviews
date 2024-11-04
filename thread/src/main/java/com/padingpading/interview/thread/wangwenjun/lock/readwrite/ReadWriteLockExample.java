package com.padingpading.interview.thread.wangwenjun.lock.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * 读写锁
 */
public class ReadWriteLockExample {
    
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    
    private int data;
    
    public void writeData(int data) {
        rwl.writeLock().lock(); // 获取写锁
        try {
            this.data = data;
        } finally {
            rwl.writeLock().unlock(); // 释放写锁
        }
    }
    
    public int readData() {
        rwl.readLock().lock(); // 获取读锁
        try {
            return data;
        } finally {
            rwl.readLock().unlock(); // 释放读锁
        }
    }
    
    public static void main(String[] args) {
        ReadWriteLockExample example = new ReadWriteLockExample();
        
        // 写入数据
        example.writeData(123);
        
        // 读取数据
        int result = example.readData();
        System.out.println("Read data: " + result);
    }
}
