package com.leopold.modules.chat.service;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.roles.ChatRole;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;

@Transactional
public interface ChatService extends ChatUserService {
    ChatEntity getById(Long chatId);
    ChatEntity create(String name, UserEntity creator, Collection<UserEntity> users);
    void delete(ChatEntity chat);
    void deleteById(Long chatId);
    void updatePicture(ChatEntity chat, FileEntity picture);
    void updateChatName(ChatEntity chat, String chatName);
    long countUnreadMessages(ChatEntity chat, UserEntity user, Timestamp origin);
    void addChatUserRole(ChatEntity chat, UserEntity user, ChatRole role);
}
