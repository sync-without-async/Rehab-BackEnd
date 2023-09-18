package com.hallym.rehab.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class SocketHandler extends TextWebSocketHandler {

    ConcurrentHashMap<UUID, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        URI uri = session.getUri();
        String path = uri.getPath();
        String uuid = path.substring(path.lastIndexOf('/') + 1);
        UUID rno = UUID.fromString(uuid);

        List<WebSocketSession> sessionsInRoom = roomSessions.get(rno);

        if(sessionsInRoom != null) {
            for (WebSocketSession webSocketSession : sessionsInRoom) {
                // 세션이 열려있고, 자신의 세션이 아닌 다른 사용자의 세션일 경우 메세지 전달.
                if (webSocketSession.isOpen() && !session.getId().equals(webSocketSession.getId())) {
                    // ice, offer, answer 를 세션에 연결된 각각의 client 에 전달
                    webSocketSession.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established: session ID = {}", session.getId());
        URI uri = session.getUri();
        String path = uri.getPath();
        String uuid = path.substring(path.lastIndexOf('/') + 1);

        UUID rno = UUID.fromString(uuid);

        List<WebSocketSession> sessionsInRoom = roomSessions.computeIfAbsent(rno, k -> new CopyOnWriteArrayList<>());
        sessionsInRoom.add(session);

        log.info("Number of connections in room {}: {}", rno, sessionsInRoom.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed: session ID = {}, status = {}", session.getId(), status);
        URI uri = session.getUri();
        String path = uri.getPath();
        String uuid = path.substring(path.lastIndexOf('/') + 1);
        UUID rno = UUID.fromString(uuid);

        List<WebSocketSession> sessionsInRoom = roomSessions.get(rno);

        if(sessionsInRoom != null){
            sessionsInRoom.remove(session);
            log.info("Number of connections in room {}: {}",rno,sessionsInRoom.size());
        }
    }
}
