package com.leopold.config;

import com.leopold.modules.security.websocket.ConnectionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(3)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ConnectionInterceptor connectionInterceptor;

    @Autowired
    public WebSocketConfig(ConnectionInterceptor connectionInterceptor) {
        this.connectionInterceptor = connectionInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app")
                .enableSimpleBroker("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(connectionInterceptor);
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(connectionInterceptor)
                .withSockJS();
    }
}
