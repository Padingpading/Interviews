package com.padingpading.java.jdk9.a_collection;


/**
 * 抽象方法:
 * 静态方法:
 *    1、可以定义通用的工具方法，使得我们可以在不同的实现类中重复使用这些方法。
 * 默认方法:
 *    1、为接口添加新的方法，而不会破坏已有代码的兼容性。
 *    2、允许接口提供默认实现，从而减少实现类的工作量。
 * 私有方法:
 *    1、解决默认方法,代码重复问题。
 */
public class C_Interce_method {
    
    public static void main(String[] args) {
        ChinaPeople chinaPeople = new ChinaPeople();
        chinaPeople.sleep();
        chinaPeople.eat();
        chinaPeople.doXxx();
    }
    
}
class ChinaPeople implements People {
    
    @Override
    public void sleep() {
        System.out.println("躺着睡");
    }
}

interface People {
    
    void sleep();
    
    default void eat() {
        drink();
    }
    
    default void doXxx() {
        drink();
    }
    /**
     * 私有方法
     */
    private void drink() {
        System.out.println("喝水");
    }
}
