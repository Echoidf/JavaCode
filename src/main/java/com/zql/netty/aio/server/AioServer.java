package com.zql.netty.aio.server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author：zql
 * @date: 2023/4/21
 */
public class AioServer extends Thread {
    // 异步套接字信道
    private AsynchronousServerSocketChannel serverSocketChannel;

    @Override
    public void run() {
        try {
            /*
             * 1. open方法创建异步服务器socket通道
             * 2. AsynchronousChannelGroup.withCachedThreadPool方法创建一个线程池（最多10个线程）作为异步通道组的执行器
             */
            serverSocketChannel = AsynchronousServerSocketChannel.open(AsynchronousChannelGroup.withCachedThreadPool(Executors.newCachedThreadPool(), 10));
            // 绑定端口进行监听
            serverSocketChannel.bind(new InetSocketAddress(7397));
            System.out.println("zql netty-demo server start done.");
            // 等待
            CountDownLatch latch = new CountDownLatch(1);
            // 当有连接时进行异步socket通道的初始化并由AioServerHandler进行回调处理数据
            serverSocketChannel.accept(this, new AioServerChannelInitializer());
            // 使当前线程进入等待状态直到计数器变为0（也就是有连接请求到来并被处理）才会继续执行
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AsynchronousServerSocketChannel serverSocketChannel() {
        return serverSocketChannel;
    }

    public static void main(String[] args) {
        new AioServer().start();
    }
}
