package com.zql.netty.samples;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class NettyServer {
    public static void main(String[] args) {
        new NettyServer().bing(7397);
    }

    private void bing(int port){
        //配置服务端的NIO线程组

        //parentGroup用于接受传入的链接请求
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        //childGroup用于处理已接受链接的流量
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)  //非阻塞模式
                    // 设置通道选项，SO_BACKLOG用于设置等待连接的队列的最大长度
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //子组创建新的channel时会执行初始化方法
                    .childHandler(new MyChannelInitializer());

            //绑定端口号，sync方法会阻塞当前线程直到绑定完成
            //ChannelFuture对象可以用于添加监听器，以便在绑定完成时通知应用程序
            ChannelFuture f = bootstrap.bind(port).sync();

            System.out.println("zql netty-demo server start done.");

            //阻塞当前线程直到服务器被关闭
            f.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }

    }
}
