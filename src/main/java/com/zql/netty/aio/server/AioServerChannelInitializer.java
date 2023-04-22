package com.zql.netty.aio.server;

import com.zql.netty.aio.ChannelInitializer;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author：zql
 * @date: 2023/4/21
 */
public class AioServerChannelInitializer extends ChannelInitializer {
    /**
     * 实现异步socket通道的初始化工作
     * ByteBuffer分配1024字节缓冲区
     * 设置超时时间为10s
     * AioServerHandler对象作为回调函数处理客户端请求，并将字节流按照GBK编码格式转为字符流
     */
    @Override
    protected void initChannel(AsynchronousSocketChannel channel) throws Exception {
        channel.read(ByteBuffer.allocate(1024), 10, TimeUnit.SECONDS, null, new AioServerHandler(channel, Charset.forName("GBK")));
    }
}
