package com.padingpading.java.jdk14;

import org.junit.Test;

import java.util.ArrayList;

/**
 * 在 Java 14 之前，使用 instanceof 进行类型判断之后，需要进行对象类型转换后才能使用。
 * d而在 Java 14 中，可以在判断类型时指定变量名称进行类型转换，方便了使用。
 */
public class A_instanceof {
    
    @Test
    public void before() {
        Object obj = new ArrayList<>();
        if (obj instanceof ArrayList) {
            ArrayList list = (ArrayList)obj;
            list.add("www.wdbyte.com");
        }
        System.out.println(obj);
    }
    
    @Test
    public void after() {
        Object obj = new ArrayList<>();
        if (obj instanceof ArrayList list) {
            //定义类型,可以直接使用
            list.add("www.wdbyte.com");
        }
        System.out.println(obj);
    }
}
