package com.leopold.modules.security.chatAuthorization;

import com.leopold.modules.security.jwt.JwtTokenFilter;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.chat.service.ChatService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ChatAuthorizationConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final ChatService chatService;

    public ChatAuthorizationConfigurer(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(new ChatAuthorizationFilter(chatService), UsernamePasswordAuthenticationFilter.class);
    }
}
