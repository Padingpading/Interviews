package com.padingpading.spring.hystrix;

/**
 * @author libin
 * @description
 * @date 2022/9/14
 */
public class Test {
    
    public static void main(String[] args) throws Exception {
        CommandHello commandHello= new CommandHello("thread_pool","hello",5);
        commandHello.execute();
    }
    
}
