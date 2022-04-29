package com.padingpading.interview.jvm.a_loading;

/**
 * 类 初始化
 */
public class ClassInit2 {
    
    private static int num = 1;

    static {
        num = 2;
        number = 20;
    }
    //linking之prepare:number=0-->inital:20->10
    private static int number = 10;
    public static void main(String[] args) {
        System.out.println(ClassInit2.num);
        System.out.println(number);
    }
}
