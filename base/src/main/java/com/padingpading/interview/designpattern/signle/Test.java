package com.padingpading.interview.designpattern.signle;

public class Test {
    
    
    public static void main(String[] args) {
        System.out.println("heelo");
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        StaticSingletonObject.StaticSingletonInnerObject instatnce = StaticSingletonObject.getInstatnce();
    }
}
