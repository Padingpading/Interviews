package com.padingpading.interview.jvm.stack;

/**
 * @author libin
 * @description
 * @date 2021-07-08
 */
public class StackException {
    public static void main(String[] args) {
        method1();
    }

    public static void method1() {
        int i = 1;
        method2();
    }

    public static void method2() {
        try {
            System.out.println("123123");
        } catch (Exception e) {
            e.getMessage();        }
    }
}
