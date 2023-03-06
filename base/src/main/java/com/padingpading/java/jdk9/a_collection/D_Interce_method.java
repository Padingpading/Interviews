package com.padingpading.java.jdk9.a_collection;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 钻石操作符升级:拓展匿名内部类
 *
 */
public class D_Interce_method {
    
    @Test
    public void diamondOperator(){
        //jdk6
        List<String> list6 = new ArrayList<String>();
        //jdk8
        List<String> jdk8 = new ArrayList<>();
        //jdk9 拓展匿名内部类
        List<String> set = new ArrayList<>(){
            @Override
            public int size() {
                return 1;
            }
        };
        //编译通过
        set.add("MM");
        set.add("JJ");
        set.add("GG");
        set.add("DD");
        int size = set.size();
        System.out.println(size);
        for(String s : set){
            System.out.println(s);
        }
    }
}
