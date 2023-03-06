package com.padingpading.java.jdk11;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class C_Var_Lambada {
    
    @Test
    public void repeat() throws IOException {
        var hashMap = new HashMap<String, Object>();
        hashMap.put("wechat", "wn8398");
        hashMap.put("website", "https://www.wdbyte.com");
        hashMap.forEach((var k, var v) -> {
            System.out.println(k + ": " + v);
        });
    }
    
}
