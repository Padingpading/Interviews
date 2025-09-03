package com.padingpading.interview.base.a_byte;

/**
 * @author libin
 * @description
 * @date 2025/8/29
 */
public class ByteTest {
    
    public static void main(String[] args) {
//        1111111
        byte a = 127;
        byte c = -128;
        Byte b = new Byte(a);
        String s = Integer.toBinaryString(b);
        String ss = Integer.toBinaryString(c);
        System.out.println(s);
        System.out.println(ss);
        System.out.println(a);
    }
}
