package com.zql.netty.bio.client;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class BioClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.33.1", 7379);
            System.out.println("zql-netty-demo bio client  start done.");
            BioClientHandler bioClientHandler = new BioClientHandler(socket, Charset.forName("utf-8"));
            bioClientHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
