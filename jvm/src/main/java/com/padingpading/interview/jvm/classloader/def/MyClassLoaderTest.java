package com.padingpading.interview.jvm.classloader.def;

/**
 * @author libin
 * @description
 * @date 2021-07-15
 */
public class MyClassLoaderTest {
    public static void main(String[] args) {
       MyClassLoader loader = new MyClassLoader("I:\\学习\\JVM\\JVM1\\src\\main\\java\\com\\padingpading\\jvm\\classloader\\");

        try {
            Class clazz = loader.loadClass("Demo");
            System.out.println("加载此类的类的加载器为：" + clazz.getClassLoader().getClass().getName());//com.dsh.jvmp2.chapter04.java2.MyClassLoader

            System.out.println("加载当前Demo1类的类的加载器的父类加载器为：" + clazz.getClassLoader().getParent().getClass().getName());//sun.misc.Launcher$AppClassLoader
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
