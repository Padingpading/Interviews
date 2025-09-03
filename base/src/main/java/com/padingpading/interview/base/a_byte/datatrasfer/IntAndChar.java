package com.padingpading.interview.base.a_byte.datatrasfer;

/**
 * @author libin
 * @description
 * @date 2025/8/29
 */
public class IntAndChar {
    
    public static void main(String[] args) {
        
        //
//        charToInt();
//        intToChar();
//        IntegerToChar();
        charToInteger();
        int num = 5; num = num++;
        System.out.println(num);
    }
    
    private static void charToInteger() {
        char c = 'a';
        int b= 4;
        b = c;
        System.out.println(b);
    }
    
    private static void IntegerToChar() {
        char c = 'a';
        int b = 103;
        c =(char)b;
        System.out.println(c);
    }
    
    private static void intToChar() {
//       超过范围值 编译报错。
        char c = 10000;
        System.out.println(c);
        
    }
    
    private static void charToInt() {
//        字符常量赋给整型变量
        int i = 'a';
    }
}
