package com.padingpading.spring.hystrix.limit;

/**
 * @author libin
 * @description
 * @date 2022/9/18
 */
public class LimitTest {
    
    public static void main(String[] args) {
        new Thread(()->{   for (int i = 0; i < 10; i++) {
            CommandLimitFailure commandFallbackFailure = new CommandLimitFailure("2");
            String execute = commandFallbackFailure.execute();
            System.out.println("执行"+execute);
        }}).start();
        new Thread(()->{   for (int i = 0; i < 8; i++) {
            CommandLimitFailure commandFallbackFailure = new CommandLimitFailure("2");
            String execute = commandFallbackFailure.execute();
            System.out.println("等待"+execute);
        }}).start();
        new Thread(()->{   for (int i = 0; i < 3; i++) {
            CommandLimitFailure commandFallbackFailure = new CommandLimitFailure("2");
            String execute = commandFallbackFailure.execute();
            System.out.println("拒绝"+execute);
        }}).start();
     
 
    }
    
}
