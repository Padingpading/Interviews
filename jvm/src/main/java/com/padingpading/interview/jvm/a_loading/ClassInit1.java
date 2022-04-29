package com.padingpading.interview.jvm.a_loading;

/**
 * 类 初始化
 */
public class ClassInit1 {
    //number=0->1->2
    private static int num = 1;

    static {
        num = 2;
    }
    public static void main(String[] args) {
        System.out.println(ClassInit1.num);
    }
}
