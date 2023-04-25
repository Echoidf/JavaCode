package com.zql.netty.aio.client;

import com.zql.netty.aio.ChannelAdapter;
import com.zql.netty.aio.ChannelHandler;

import java.io.IOException;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author：zql
 * @date: 2023/4/21
 */
public class AioClientHandler extends ChannelAdapter {
    public AioClientHandler(AsynchronousSocketChannel channel, Charset charset) {
        super(channel, charset);
    }

    @Override
    public void channelActive(ChannelHandler ctx) {
        try {
            System.out.println("链接报告信息:" + ctx.channel().getRemoteAddress());
            //通知客户端链接建立成功
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelInactive(ChannelHandler ctx) {
    }

    @Override
    public void channelRead(ChannelHandler ctx, Object msg) {
        System.out.println("客户端收到：" + new Date() + " " + msg + "\r\n");
        ctx.writeAndFlush("客户端信息处理Success！\r\n");
    }
}
