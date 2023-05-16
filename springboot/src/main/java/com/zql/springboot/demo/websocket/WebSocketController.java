package com.zql.springboot.demo.websocket;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author：zql
 * @date: 2023/5/16
 */
@RestController
@RequestMapping("/ws")
@Api(tags = "websocket请求")
public class WebSocketController {
    /**
     * 请求测试
     */
    @GetMapping("/index")
    @ApiOperation("ping")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("请求成功");
    }

    /**
     * 客户端请求demo
     */
    @GetMapping("/page")
    @ApiOperation("客户端请求")
    public ModelAndView page(){
        return new ModelAndView("websocket");
    }

    /**
     * 服务器主动推送demo
     */
    @PostMapping("/push/{toUserId}")
    @ApiOperation("服务器主动推送")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message,toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
