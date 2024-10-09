package com.padingpading.java.jdk10;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class C_Collection {
    
    /**
     * 比如可以通过 Collection.copyOf 复制得到一个不可改变集合，即使原来的集合元素发生了变化也不会有影响。
     */
    @Test
    public void copyOf() {
        var list = new ArrayList<String>();
        list.add("wechat");
        list.add("wn8398");
        List<String> copyList = List.copyOf(list);
        list.add("test");
        System.out.println(copyList);
        // result
        // [wechat, wn8398]
    }
    
    /**
     * 也为 Optional 增加了一个新的方法 orElseThrow。调用这个方法也可以获取到 optional 中的 value , 但是如果 value 为 null ，就会抛出异常。
     */
    @Test
    public void Optional() {
        String s = null;
        Object o = Optional.of(s).orElseThrow();
        System.out.println(o);
    }
}
