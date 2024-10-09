package com.padingpading.java.jdk9.a_collection;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 集合工厂方法
 * 在 Java 9 中为集合的创建增加了静态工厂创建方式，也就是 of 方法，通过静态工厂 of 方法创建的集合是只读集合，
 * 里面的对象不可改变。并在不能存在 null 值，对于 set 和 map 集合，也不能存在 key 值重复。
 * 这样不仅线程安全，而且消耗的内存也更小。
 */
public class A_Colleciton_Factory {
    
    @Test
    public void example(){
        // 工厂方法创建集合
        //ImmutableCollections.ListN
        List<String> stringList = List.of("a", "b", "c", "d");
        //ImmutableCollections.SetN
        Set<String> stringSet = Set.of("a", "b", "c", "d");
        //ImmutableCollections.MapN<>
        Map<String, Integer> stringIntegerMap = Map.of("key1", 1, "key2", 2, "key3", 3);
        Map<String, Integer> stringIntegerMap2 = Map.ofEntries(Map.entry("key1", 1), Map.entry("key2", 2));
        // 集合输出
        System.out.println(stringList);
        System.out.println(stringSet);
        System.out.println(stringIntegerMap);
        System.out.println(stringIntegerMap2);
    }
    @Test
    public void implJava8(){
        //这种只读集合在 Java 9 之前创建是通过 Collections.unmodifiableList 修改集合操作权限实现的。
        List<String> arrayList = new ArrayList<>();
        arrayList.add("达西");
        arrayList.add("程序猿阿朗");
        // 设置为只读集合
        List<String> unmodifiableList = Collections.unmodifiableList(arrayList);
        System.out.println(unmodifiableList);
    }
    
    /**
     * 工厂可以自由创建新的实例或者复用现有实例，所以 使用 of 创建的集合，
     * 避免 == 或者 hashCode 判断操作
     * 这也是使用 of 方法创建集合的优势之一，消耗更少的系统资源。这一点也体现在 of 创建的集合的数据结构实现上
     */
    @Test
    public void hashCodeChange(){
        ArrayList<String>  a = new ArrayList<>();
        ArrayList<String>  b = new ArrayList<>();
        a.add("a");//128
        b.add("b");//129
    
        //工厂可以自由创建新的实例或者复用现有实例，所以 使用 of 创建的集合，避免 == 或者 hashCode 判断操作
        List<String> stringList1 = List.of("a", "b", "c", "d");
        List<String> stringList2 = List.of("a", "b", "c", "d");
        System.out.println(stringList1);//3910595
        System.out.println(stringList2);//3910595
    }
}
