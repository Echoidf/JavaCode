package com.zql.springboot.demo.netty;

import com.zql.springboot.demo.netty.server.NettyServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author：zql
 * @date: 2023/4/26
 */
@RestController
@RequestMapping(value = "/netty", method = RequestMethod.GET)
@Api(tags = "netty测试")
public class NettyController {

    @Resource
    private NettyServer nettyServer;

    @RequestMapping("/localAddress")
    @ApiOperation(value = "获取NettyServer本地地址")
    public String localAddress() {
        return "nettyServer localAddress " + nettyServer.getChannel().localAddress();
    }

    @RequestMapping("/isOpen")
    @ApiOperation(value = "判断NettyServer是否打开")
    public String isOpen() {
        return "nettyServer isOpen " + nettyServer.getChannel().isOpen();
    }
}
