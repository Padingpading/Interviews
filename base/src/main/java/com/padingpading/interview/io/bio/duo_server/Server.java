package com.padingpading.interview.io.bio.duo_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) {
        try {
            //1、创建socket对象请求服务端的链接
            ServerSocket ss =new ServerSocket(9999);
            //2、定义一个死循环，负责不断的接受客户端的socket链接请求
            while (true){
                Socket socket = ss.accept();
                //3、创建一个独立的线程俩处理这个客户端的socket通信需求
                new ServerThread(socket).start();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
