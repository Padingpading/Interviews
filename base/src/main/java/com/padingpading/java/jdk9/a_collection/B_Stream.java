package com.padingpading.java.jdk9.a_collection;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**Stream 新方法
 *
 */
public class B_Stream {
    
    /**
     * takeWhile: 从头开始筛选，遇到不满足的就结束了。
     */
    @Test
    public void takeWhile(){
        // takeWhile ,从头开始筛选，遇到不满足的就结束了
        List<Integer> list1 = List.of(1, 2, 3, 4, 5);
        List<Integer> listResult = list1.stream().takeWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult);
        // takeWhile ,从头开始筛选，遇到不满足的就结束
        List<Integer> list2 = List.of(1, 2, 3, 4, 3, 0);
        List<Integer> listResult2 = list2.stream().takeWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult2);
    }
    
    /**
     * dropWhile: 从头开始删除，遇到不满足的就结束了。
     */
    @Test
    public void dropWhile(){
        // dropWhile ,从头开始删除，遇到不满足的就结束了
        List<Integer> list1 = List.of(1, 2, 3, 4, 5);
        List<Integer> listResult = list1.stream().dropWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult);
        // dropWhile ,从头开始删除，遇到不满足的就结束
        List<Integer> list2 = List.of(1, 2, 3, 4, 3, 0);
        List<Integer> listResult2 = list2.stream().dropWhile(x -> x < 3).collect(Collectors.toList());
        System.out.println(listResult2);

    }
    
    /**
     * ofNullable: 创建支持全 null 的 Stream.
     */
    @Test
    public void ofNullable(){
        Stream<Integer> stream = Stream.of(1, 2, null);
        stream.forEach(System.out::print);
        System.out.println();
        // 空指针异常
        // stream = Stream.of(null);
        stream = Stream.ofNullable(null);
        stream.forEach(System.out::print);
    }
    
    /**
     * iterate: 可以重载迭代器。
     */
    @Test
    public void iterate(){
        //0123456789
        IntStream.iterate(0, x -> x < 10, x -> x + 1).forEach(System.out::print);
    }
}
