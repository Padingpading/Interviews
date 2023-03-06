package com.padingpading.java.jdk11;


import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class B_File {
    
    @Test
    public void repeat() throws IOException {
        // 创建临时文件
        Path path = Files.writeString(Files.createTempFile("test", ".txt"), "https://www.wdbyte.com");
        System.out.println(path);
        // 读取文件
        // String ss = Files.readString(Path.of("file.json"));
        String s = Files.readString(path);
        System.out.println(s);
        // 结果
        // https://www.wdbyte.com
    }
    
}
