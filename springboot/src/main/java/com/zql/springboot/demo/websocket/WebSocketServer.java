package com.zql.springboot.demo.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author：zql
 * @date: 2023/5/16
 */
@ServerEndpoint("/imserver/{userId}")
@Component
public class WebSocketServer {
    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        }else{
            webSocketMap.put(userId,this);
            onlineCount.getAndIncrement();
        }
        logger.info("用户连接:" + userId + ",当前在线人数为:" + onlineCount.get());
        try{
            sendMessage("连接成功");
        }catch (IOException e){
            logger.error("用户：" + userId + ",网络异常！");
        }
    }

    private void sendMessage(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        if (webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            onlineCount.getAndDecrement();
        }
        logger.info("用户退出:"+userId+",当前在线人数为:" + onlineCount.get());
    }

    @OnMessage
    public void onMessage(String message, Session session)  {
        logger.info("用户{}消息：{}",userId, message);
        if (StringUtils.hasText(message)){
            JSONObject jsonObject = JSON.parseObject(message);
            //追加发送人，防止篡改
            jsonObject.put("fromUserId", this.userId);
            String toUserId = jsonObject.getString("toUserId");
            if (StringUtils.hasText(toUserId) && webSocketMap.containsKey(toUserId)){
                try {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                logger.error("请求的用户{}不在该服务器上或已下线",toUserId);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        logger.info("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.hasText(userId)&&webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            logger.error("用户"+userId+",不在线！");
        }
    }
}
