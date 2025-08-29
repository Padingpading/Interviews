package com.padingpading.interview.io.bio.dan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) {
        try {
            //1、定义一个server socket 对象进行服务器端的端口注册
            ServerSocket serverSocket =new ServerSocket(9999);
            //2、监听客户端的socket链接请求,这里会被阻塞，需要等待client端的链接。三次握手。
            Socket socket = serverSocket.accept();
            //3、从socket管道中得到一个字节输入流对象
            InputStream is = socket.getInputStream();
            //4、把字节输入流包装成一个缓冲字符输入流
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String msg;
            if ((msg=bf.readLine())!=null){
                System.out.println("服务端接口:"+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
