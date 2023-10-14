package com.leopold.modules.chat.ws;

import com.leopold.modules.chat.service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ChatAuthInterceptor implements HandshakeInterceptor {
    private static final Pattern URI_PATTERN = Pattern.compile("/chat/(\\d+)/(\\d+)");
    private final ChatUserService chatUserService;

    @Autowired
    public ChatAuthInterceptor(ChatUserService chatUserService) {
        this.chatUserService = chatUserService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String requestURI = request.getURI().toString();

        Matcher matcher = URI_PATTERN.matcher(requestURI);
        if (matcher.find()) {
            Long chatId = Long.valueOf(matcher.group(1));
            Long userId = Long.valueOf(matcher.group(2));
            Long reqUserId = (Long)attributes.get("reqUserId");
            return userId.equals(reqUserId) && chatUserService.checkUserInChat(chatId, userId);
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
