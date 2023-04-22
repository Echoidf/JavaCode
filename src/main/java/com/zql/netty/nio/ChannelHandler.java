package com.zql.netty.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class ChannelHandler {
    private SocketChannel channel;
    private Charset charset;

    public ChannelHandler(SocketChannel channel, Charset charset) {
        this.channel = channel;
        this.charset = charset;
    }

    public void writeAndFlush(Object msg){
        try{
            byte[] bytes = msg.toString().getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public SocketChannel channel(){
        return channel;
    }
}
