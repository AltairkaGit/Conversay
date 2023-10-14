package com.leopold.modules.security.chatAuthorization;

public interface ChatAuthorizationService {
    Long extractChatIdFromURI(String uri);
}
