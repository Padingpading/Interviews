package com.padingpading.interview.jvm.classloader.c_namespace;

import com.padingpading.interview.jvm.classloader.Demo;
import com.padingpading.interview.jvm.classloader.b_cus.load.MyClassLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 *
 */
public class NameSpace1 {
    
    public static void main(String[] args)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        MyClassLoader loader1 = new MyClassLoader(
                "E:\\学习\\Interviews\\jvm\\target\\classes\\com\\padingpading\\interview\\jvm\\classloader\\");
        MyClassLoader loader2 = new MyClassLoader(
                "E:\\学习\\Interviews\\jvm\\target\\classes\\com\\padingpading\\interview\\jvm\\classloader\\");
        Class clazz1 = loader1.loadClass("Demo");
        Class clazz2 = loader2.loadClass("Demo");
        //两个类加载器不可见。
        System.out.println(clazz1 == clazz2);
        //loader1
        Object o1 = clazz1.newInstance();
        //loader2
        Object o2 = clazz2.newInstance();
        //app loader
        Demo demo = new Demo();
        System.out.println(demo.getClass().getClassLoader());
 
    }
    
}
