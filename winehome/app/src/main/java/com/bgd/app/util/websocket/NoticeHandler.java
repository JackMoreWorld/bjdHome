package com.bgd.app.util.websocket;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class NoticeHandler extends AbstractWebSocketHandler {


    private static ConcurrentHashMap<WebSocketSession, UserSession> users = new ConcurrentHashMap<>();

    private volatile static NoticeHandler noticeHandler;

    private NoticeHandler() {
    }

    public static NoticeHandler getInstance() {
        if (noticeHandler == null) {
            synchronized (NoticeHandler.class) {
                if (noticeHandler == null) {
                    noticeHandler = new NoticeHandler();
                }
            }
        }
        return noticeHandler;
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String text = message.getPayload();
        UserSession user = JSONObject.parseObject(text, UserSession.class);
        System.out.println("---------->"+text);
        synchronized (users) {
            users.put(session, user);
        }
    }

    public void afterConnectionEstablished(WebSocketSession session) {
        String sessionId = session.getId();
        log.debug("有新连接加入：" + sessionId);
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        synchronized (users) {
            if (users.containsKey(session)) {
                UserSession userSession = users.get(session);
                users.remove(userSession);
            }
        }
        log.debug("有连接退出：" + session.getId());
    }

    public static void sendToSingle(UserSession user, String json) {
        List<WebSocketSession> list = new ArrayList<>();
        synchronized (users) {
            for (Map.Entry<WebSocketSession, UserSession> entry : users.entrySet()) {
                if (entry.getValue().equals(user.getPhone())) {
                    list.add(entry.getKey());
                }
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            log.info("没有需要通知的对象");
            return;
        }
        TextMessage message = new TextMessage(json);
        for (WebSocketSession session : list) {
            try {
                log.info("向" + user.getPhone() + "，推送相关消息：" + json);
                session.sendMessage(message);
            } catch (Exception e) {
                log.error("用户：" + user.getPhone() + "推送数据异常：", e);
            }
        }
    }


    public static void sendToAll(String json) {
        log.info("全局推送消息:{} " , json);
        List<WebSocketSession> list = new ArrayList<>();
        synchronized (users) {
            list = users.keySet().stream().collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(list)) {
            log.info("没有需要通知的对象");
            return;
        }
        TextMessage message = new TextMessage(json);
        for (WebSocketSession session : list) {
            try {
                log.info("向" + session.getId() + " 推送数据：" + json);
                session.sendMessage(message);
            } catch (Exception e) {
                log.error("向" + session.getId() + " 推送数据异常：", e);
            }
        }
    }
}
