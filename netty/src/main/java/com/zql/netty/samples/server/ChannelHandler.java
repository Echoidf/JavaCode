package com.zql.netty.samples.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author：zql
 * @date: 2023/4/22
 */
public class ChannelHandler {

    //用于存放用户channel信息，也可以使用ConcurrentHashMap来模拟不同的群组
    public static  ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}

