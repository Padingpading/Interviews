package com.padingpading.interview.collection.a_list;

import java.util.ArrayList;

/**
 * @author libin
 * @description
 * @date 2022-04-24
 */
public class ArrayListSerial {
    
    public static void main(String[] args) {
         ArrayList<Object> objects = new ArrayList<>();
         objects.add(new Object());
         objects.add(new Object());
         objects.add(new Object());
         objects.add(new Object());
         
         //发送list到 server
        //list序列化(writeObject)
        //transient object[] elementData
        //server端收到list
        //list反序列化 (readObject)
        
    }
    
}
