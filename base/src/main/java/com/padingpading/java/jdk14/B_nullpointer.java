package com.padingpading.java.jdk14;

/**
 *NullPointerException 一直都是一个比较常见的异常，但是在 Java 14 之前，如果一行有多个表达式时，这时报了空指针后，单纯的从报错信息来看，可能并不知道是哪个对象为 NULL
 */
public class B_nullpointer {
    
    public static void main(String[] args) {
        String content1 = "www.wdbyte.com";
        String content2 = null;
        int length = content1.length() + content2.length();
        System.out.println(length);
//        Exception in thread "main" java.lang.NullPointerException: Cannot invoke "String.length()" because "content2" is null
//        at com.padingpading.java.jdk14.B_nullpointer.main(B_nullpointer.java:12)
    }
    
}
