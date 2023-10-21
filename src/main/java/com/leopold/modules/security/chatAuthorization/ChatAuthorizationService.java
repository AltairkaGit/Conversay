package com.leopold.modules.security.chatAuthorization;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.user.entity.UserEntity;

public interface ChatAuthorizationService {
    Long extractChatIdFromURI(String uri);
    boolean checkUserInChat(ChatEntity chat, UserEntity user);
    boolean checkUserInChat(Long chatId, Long userId);
}
