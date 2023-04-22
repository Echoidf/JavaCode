package com.zql.netty.samples;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.Charset;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        /**
         * 添加解码器
         * LineBasedFrameDecoder  基于换行符
         * 注意：这种方式如果发送的消息没有换行会存储在ByteBuf中而不会执行channelRead方法
         * 常见的针对字符串的解码器还有：
         * DelimiterBasedFrameDecoder 基于指定字符串,下面这样写等价于LineBasedFrameDecoder
         * channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024，false, Delimiters.lineDelimiter()));
         *
         * FixedLengthFrameDecoder 基于字符串长度
         */
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        //解码转String，注意字符串的编码格式
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        //在管道中添加接受数据的方法
        channel.pipeline().addLast(new MyServerHandler());
    }
}

