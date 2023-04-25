package com.zql.netty.samples.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器
 *
 * @author：zql
 * @date: 2023/4/23
 */
public class MyEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf out) throws Exception {
        String message = in.toString();
        byte[] bytes = message.getBytes("GBK");

        byte[] send = new byte[bytes.length + 2];
        System.arraycopy(bytes, 0, send, 1, bytes.length);
        send[0] = 0x02; //开始
        send[send.length-1] = 0x03; //末尾

        out.writeInt(send.length);
        out.writeBytes(send);
    }
}
