package com.padingpading.spring.hystrix.fallback;

/**
 * @author libin
 * @description
 * @date 2022/9/18
 */
public class FallBackTest {
    
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            CommandFallbackFailure commandFallbackFailure = new CommandFallbackFailure("2");
            String execute = commandFallbackFailure.execute();
            System.out.println(execute);
        }
 
    }
    
}
