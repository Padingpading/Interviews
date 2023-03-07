package com.padingpading.java.jdk12;

import org.junit.Test;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

/**
  *  简化的数字格式可以直接转换数字显示格式，
 *  比如 1000 -> 1K，1000000 -> 1M 。
 *  例如 10000-> 1万
 *
 * */
public class C_Compact {
    
    @Test
    public void repeat() throws IOException {
        System.out.println("Compact Formatting is:");
        NumberFormat upvotes = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
     
        System.out.println(upvotes.format(100));
        System.out.println(upvotes.format(1000));
        System.out.println(upvotes.format(10000));
        System.out.println(upvotes.format(100000));
        System.out.println(upvotes.format(1000000));
    
        // 设置小数位数
        upvotes.setMaximumFractionDigits(1);
        System.out.println(upvotes.format(1234));
        System.out.println(upvotes.format(123456));
        System.out.println(upvotes.format(12345678));
//        100
//        1K
//        10K
//        100K
//        1M
//        1.2K
//        123.5K
//        12.3M
    }
    
    @Test
    public void china() throws IOException {
        System.out.println("Compact Formatting is:");
        NumberFormat upvotes = NumberFormat.getCompactNumberInstance(Locale.CHINA, NumberFormat.Style.SHORT);
        
        System.out.println(upvotes.format(100));
        System.out.println(upvotes.format(1000));
        System.out.println(upvotes.format(10000));
        System.out.println(upvotes.format(100000));
        System.out.println(upvotes.format(1000000));
        
        // 设置小数位数
        upvotes.setMaximumFractionDigits(1);
        System.out.println(upvotes.format(1234));
        System.out.println(upvotes.format(123456));
        System.out.println(upvotes.format(12345678));
    }

}
