package com.padingpading.java.jdk14;

/**
 *
 */
public class C_Records {
    
    public static void main(String[] args) {
        Dog dog1 = new Dog("牧羊犬", 1);
        Dog dog2 = new Dog("田园犬", 2);
        Dog dog3 = new Dog("哈士奇", 3);
        System.out.println(dog1);
        System.out.println(dog2);
        System.out.println(dog3);
    }
    
    public record Dog(String name, Integer age) {
    
    }
}
