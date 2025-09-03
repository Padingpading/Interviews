package com.padingpading.interview.designpattern.signle;


/**
 * 静态内部类
 */
public class StaticSingletonObject {
    private static  volatile StaticSingletonObject instance;
    
    static {
        System.out.println("123");
    }
    
    private  StaticSingletonObject() {
    }
    public  static class StaticSingletonInnerObject {
        public static final StaticSingletonInnerObject staticSingletonInnerObject =  new StaticSingletonInnerObject();
    }
    
    public static StaticSingletonInnerObject  getInstatnce(){
        return StaticSingletonInnerObject.staticSingletonInnerObject;
    }
}
