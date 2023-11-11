package com.leopold.modules.security.websocket;

import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.security.tokenExtractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class ConnectionInterceptor implements HandshakeInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;
    private final List<SubscribeMiddleware> subscribeMiddlewares;

    @Autowired
    public ConnectionInterceptor(JwtTokenProvider jwtTokenProvider, TokenExtractor tokenExtractor, List<SubscribeMiddleware> subscribeMiddlewares) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExtractor = tokenExtractor;
        this.subscribeMiddlewares = subscribeMiddlewares;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String access = tokenExtractor.extract(request);
        try {
            if (access == null || access.isEmpty() || !jwtTokenProvider.validateAccess(access))
                return false;
        } catch (Exception ex) {
            return false;
        }
        Long userId = jwtTokenProvider.getUserId(access);
        attributes.put("userId", userId);
        subscribeMiddlewares.forEach(middleware -> middleware.subscribe(userId));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
