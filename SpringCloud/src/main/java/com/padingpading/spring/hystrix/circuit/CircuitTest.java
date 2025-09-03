package com.padingpading.spring.hystrix.circuit;

/**
 * @author libin
 * @description
 * @date 2022/9/18
 */
public class CircuitTest {
    
    public static void main(String[] args) throws InterruptedException {
        //
        for (int i = 0; i < 15; i++) {
            CommandCircuitFailure commandFallbackFailure = new CommandCircuitFailure("1");
            String execute = commandFallbackFailure.execute();
            System.out.println(execute);
        }
        for (int i = 0; i < 25; i++) {
            CommandCircuitFailure commandFallbackFailure = new CommandCircuitFailure("2");
            String execute = commandFallbackFailure.execute();
            System.out.println(execute);
        }
        //等待时间窗口过了,统计了,进行统计
        Thread.sleep(5000);
        //断路器打开。
        for (int i = 0; i < 5; i++) {
            CommandCircuitFailure commandFallbackFailure = new CommandCircuitFailure("1");
            String execute = commandFallbackFailure.execute();
            System.out.println(execute);
        }
        Thread.sleep(5000);
        //断路器恢复
        for (int i = 0; i < 5; i++) {
            CommandCircuitFailure commandFallbackFailure = new CommandCircuitFailure("1");
            String execute = commandFallbackFailure.execute();
            System.out.println(execute);
        }
 
    }
    
}
