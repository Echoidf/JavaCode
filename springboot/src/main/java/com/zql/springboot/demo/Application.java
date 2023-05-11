package com.zql.springboot.demo;

import com.zql.springboot.demo.netty.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * @author：zql
 * @date: 2023/4/25
 */
@SpringBootApplication
@ComponentScan("com.zql.springboot.demo.netty")
public class Application implements CommandLineRunner {


    @Value("${netty.host}")
    private String host;
    @Value("${netty.port}")
    private int port;

    @Resource
    private NettyServer nettyServer;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(host, port);
        ChannelFuture channelFuture = nettyServer.bing(address);
        //用来注册一个 Thread 对象，在 JVM 停止时（正常或异常终止）执行该线程的 run() 方法。这个方法可以用来进行一些清理工作，例如关闭文件、释放资源、保存数据等等。
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }
}
