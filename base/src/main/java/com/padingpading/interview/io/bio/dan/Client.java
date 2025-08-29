package com.padingpading.interview.io.bio.dan;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //1、创建socket对象请求服务端的链接
        Socket socket = new Socket("127.0.0.1",9999);
        //2、从socket对象中获取一个字节输出流。
        OutputStream os = socket.getOutputStream();
        PrintStream ps = new PrintStream(os);
        ps.println("hello,world");
        ps.flush();
    }
}