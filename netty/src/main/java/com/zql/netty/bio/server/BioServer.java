package com.zql.netty.bio.server;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class BioServer extends Thread{
    private ServerSocket serverSocket = null;

    public static void main(String[] args) {
        BioServer bioServer = new BioServer();
        bioServer.start();
    }

    @Override
    public void run() {
        try{
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(7379));
            System.out.println("zql netty-demo bio server start done.");
            while (true){
                Socket socket = serverSocket.accept();
                BioServerHandler bioServerHandler = new BioServerHandler(socket, Charset.forName("GBK"));
                bioServerHandler.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
