package com.leopold.modules.security.chatAuthorization.impl;

import com.leopold.modules.security.chatAuthorization.ChatAuthorizationService;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ChatAuthorizationServiceImpl implements ChatAuthorizationService {
    @Override
    public Long extractChatIdFromURI(String uri) {
        Matcher matcher = chatIdPattern.matcher(uri);
        if (matcher.find()) {
            String chatIdStr = matcher.group(1);
            return Long.parseLong(chatIdStr);
        }
        return null;
    }

    private static final Pattern chatIdPattern = Pattern.compile("/chat/(\\d+)");
}
