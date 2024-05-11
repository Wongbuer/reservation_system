package com.wong.reservation.websocket;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.wong.reservation.domain.entity.ChatLog;
import com.wong.reservation.service.ChatLogService;
import com.wong.reservation.service.UserService;
import com.wong.reservation.utils.SpringContextHolder;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Wongbuer
 * @createDate 2024/4/21
 */
@Slf4j
@Component
@EqualsAndHashCode
@ServerEndpoint("/websocket/{token}")
public class ChatServerEndPoint {
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static final ConcurrentHashMap<Long, ChatServerEndPoint> WEB_SOCKET_MAP = new ConcurrentHashMap<>();
    private Session session;
    private Long id;
    private ChatLogService chatLogService;
    private UserService userService;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        long id;
        try {
            id = Long.parseLong((String) StpUtil.getLoginIdByToken(token));
        } catch (NumberFormatException e) {
            session.close();
            throw new SaTokenException("未能读取到有效token, 请登录");
        }
        this.session = session;
        this.id = id;
        ChatServerEndPoint olderConn = WEB_SOCKET_MAP.get(this.id);
        if (olderConn != null) {
            olderConn.session.close();
        }
        WEB_SOCKET_MAP.put(this.id, this);
        ONLINE_COUNT.set(WEB_SOCKET_MAP.size());
        try {
            sendMessage("conn_success");
            log.info("有新窗口开始监听:{},当前在线人数为:{}", this.id, ONLINE_COUNT.get());
            chatLogService = SpringContextHolder.getBean(ChatLogService.class);
            userService = SpringContextHolder.getBean(UserService.class);
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    @OnClose
    public void onClose() {
        if (session != null) {
            WEB_SOCKET_MAP.remove(id);
            ONLINE_COUNT.getAndDecrement();
            //断开连接情况下，更新主板占用情况为释放
            log.info("释放的id为：{}", id);
            //这里写你 释放的时候，要处理的业务
            log.info("有一连接关闭！当前在线人数为{}", ONLINE_COUNT.get());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            // 将信息转为chatLog
            ChatLog chatLog = JSONUtil.toBean(message, ChatLog.class);
            chatLog.setSenderId(id);
            chatLog.setCreatedAt(LocalDateTime.now());
            // 判断信息是否有误
            if (!isChatValid(chatLog)) {
                sendMessage("信息格式有误");
                return;
            }
            if (WEB_SOCKET_MAP.containsKey(chatLog.getReceiverId())) {
                chatLog.setStatus(1);
            }
            // 保存信息
            chatLogService.save(chatLog);
            // 向对方发送
            if (WEB_SOCKET_MAP.containsKey(chatLog.getReceiverId())) {
                WEB_SOCKET_MAP.get(chatLog.getReceiverId()).sendMessage(chatLog);
            }
        } catch (Exception e) {
            sendMessage("信息格式有误");
            return;
        }
        log.info("收到来自窗口{}的信息:{}", id, message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }

    public void sendMessage(ChatLog chatLog) {
        this.session.getAsyncRemote().sendText(JSONUtil.toJsonStr(chatLog, JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss")));
    }

    public void broadcast(String message) throws IOException {
        for (ChatServerEndPoint item : WEB_SOCKET_MAP.values()) {
            item.sendMessage(message);
        }
    }

    private boolean isChatValid(ChatLog chatLog) {
        if (ObjectUtils.isEmpty(chatLog.getReceiverId())) {
            return false;
        }
        if (!StringUtils.hasText(chatLog.getContent())) {
            return false;
        }
        return !ObjectUtils.isEmpty(userService.getById(chatLog.getReceiverId()));
    }
}


