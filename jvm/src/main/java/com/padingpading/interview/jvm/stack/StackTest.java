package com.padingpading.interview.jvm.stack;

/**
 * @author libin
 * @description
 * @date 2021-07-06
 */
public class StackTest {
    public static void main(String[] args) {
        StackTest stackTest = new StackTest();
        stackTest.methodA();
    }

    public void methodA() {
        int i = 20;
        int d = 10;
        methodB();
    }

    public void methodB() {
        int i = 20;
        int d = 10;
    }

    public static void test(int num) {
        int i = num;
        StackTest stackTest = new StackTest();
        stackTest.methodA();
    }
}
