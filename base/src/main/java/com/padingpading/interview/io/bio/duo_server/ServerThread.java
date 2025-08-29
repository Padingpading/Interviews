package com.padingpading.interview.io.bio.duo_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends  Thread {
    
    private Socket socket;
    
    public ServerThread(Socket socket){
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            int port = socket.getPort();
            //从socket对象中得到一个字节输入流
            InputStream is = socket.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String msg;
            while ((msg=bf.readLine())!=null){
                System.out.println("服务端接口:"+port+":"+msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

