package com.padingpading.interview.designpattern.signle;


/**
 * 单例双重检测
 */
public class DoubleCheckSingletonObject {
    private static  volatile DoubleCheckSingletonObject instance;
    private DoubleCheckSingletonObject(){
    }
    
    public  static DoubleCheckSingletonObject getInstance(){
        if(null==instance){
            synchronized (DoubleCheckSingletonObject.class){
                if(null==instance){
                    instance = new DoubleCheckSingletonObject();
                }
            }
        }
        return instance;
    }
}
