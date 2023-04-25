package com.zql.netty.samples.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author：zql
 * @date: 2023/4/25
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //数据编码
        socketChannel.pipeline().addLast(new HttpResponseEncoder());
        //数据解码
        socketChannel.pipeline().addLast(new HttpRequestDecoder());
        //自定义数据接收方法
        socketChannel.pipeline().addLast(new MyServerHandler());
    }
}
