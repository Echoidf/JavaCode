package com.zql.netty.samples.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author：zql
 * @date: 2023/4/25
 */
public class HttpNettyServer {

    public static void main(String[] args) {
        new HttpNettyServer().bing(7397);
    }

    private void bing(int port){
        //配置服务端NIO线程组
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        //NioEventLoopGroup extends MultiThreadEventLoopGroup Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)    //非阻塞模式
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //长连接模式
                    .childHandler(new HttpChannelInitializer());
            ChannelFuture f = b.bind(port).sync();
            System.out.println("zql-demo-netty-http server start done");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }
    }
}
