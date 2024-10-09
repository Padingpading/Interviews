package com.padingpading.java.jdk10;


import org.junit.Test;

import java.util.List;

/**
 * @author libin
 * @description
 * @date 2023-03-06
 */
public class A_Inference {
    
    public static void main(String[] args) {
        //        var hashMap = new HashMap<String, String>();
        //        hashMap.put("微信","wn8398");
        //        var string = "hello java 10";
        //        var stream = Stream.of(1, 2, 3, 4);
        //        var list = new ArrayList<String>();
        //        System.out.println(list);
    }
    
    @Test
    public void example() {
        // 情况1，没有初始化会报错
        // var list;
        var list = List.of(1, 2, 3, 4);
        // 情况2
        for (var integer : list) {
            System.out.println(integer);
        }
        // 情况3
        for (var i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
