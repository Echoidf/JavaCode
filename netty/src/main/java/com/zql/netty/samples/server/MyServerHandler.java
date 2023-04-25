package com.zql.netty.samples.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当有客户端连接时添加到channelGroup通信组
//        ChannelHandler.channelGroup.add(ctx.channel());

        SocketChannel channel = (SocketChannel)ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");

        //通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
//        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
//        buf.writeBytes(str.getBytes("GBK"));
//        ctx.writeAndFlush(buf);

        //添加stringDecoder之后就不需要再手动处理发出的数据
        ctx.writeAndFlush(str);
    }

    /**
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
        //当有客户端退出时，从channelGroup通信组中移除
//        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收msg消息
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] msgByte = new byte[buf.readableBytes()];
//        buf.readBytes(msgByte);
//        System.out.print(new Date() + "接收到消息：");
//        System.out.println(new String(msgByte, Charset.forName("GBK")));

        //添加解码器后就不需要再对消息进行格式处理
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + msg);

        //通知客户端链消息发送成功
        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";
        ctx.writeAndFlush(str);

        //向通信组发消息
//        ChannelHandler.channelGroup.writeAndFlush(str);
    }

    /**
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
