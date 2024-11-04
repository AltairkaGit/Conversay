package com.leopold.modules.security.chatAuthorization;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.roles.ChatRole;

import java.util.List;

public interface ChatAuthorizationService {
    Long extractChatIdFromURI(String uri);
    boolean checkUserInChat(ChatEntity chat, UserEntity user);
    boolean checkUserInChat(Long chatId, Long userId);
    List<ChatRole> getUserChatRoles(ChatEntity chat, UserEntity user);
    List<ChatRole> getUserChatRoles(Long chatId, Long userId);
}
