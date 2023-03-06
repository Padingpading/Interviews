package com.padingpading.java.jdk11;


import org.junit.Test;

import java.util.stream.Stream;

public class A_String {
    
    
    /**
     * 判断字符串是否为空,不能判断是否为null
     */
    @Test
    public void blank() {
        // 判空，blank里我放入了全角空格，半角空格，TAB
        String blank = "　　  ";
        System.out.println(blank.isBlank());
    }
    
    /**
     *lines () 分割获取字符串流。
     */
    @Test
    public void linesd() {
        // lines 返回一个 Stream
        String line = "a\nb\nc";
        Stream<String> lines = line.lines();
        // 使用 lambda 遍历
        lines.forEach(System.out::println);
        // 输出
        // a
        // b
        // c
    }
    
    /**
     *repeat (int n)  复制字符串(次数)
     */
    @Test
    public void repeat() {
        // 复制字符串
        String repeat = "我的微信:wn8398,";
        String repeat3 = repeat.repeat(3);
        System.out.println(repeat3);
        // 输出
        // 我的微信:wn8398,我的微信:wn8398,我的微信:wn8398,
    }
    
    /**
     * str.strip()  复制字符串(次数)
     * trim 只能去除半角空格，
     * strip 是去除各种空白符。
     */
    @Test
    public void strip () {
        // 去除前后空白
        String strip = "   　 https://www.wdbyte.com 　";
        System.out.println("==" + strip.trim() + "==");
        // 去除前后空白字符，如全角空格，TAB
        System.out.println("==" + strip.strip() + "==");
        // 去前面空白字符，如全角空格，TAB
        System.out.println("==" + strip.stripLeading() + "==");
        // 去后面空白字符，如全角空格，TAB
        System.out.println("==" + strip.stripTrailing() + "==");
        // 输出
        // ==　 https://www.wdbyte.com 　==
        // ==https://www.wdbyte.com==
        // ==https://www.wdbyte.com 　==
        // ==   　 https://www.wdbyte.com==
    }
}
