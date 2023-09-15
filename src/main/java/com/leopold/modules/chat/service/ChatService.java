package com.leopold.modules.chat.service;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional
public interface ChatService extends ChatUserService {
    ChatEntity getById(Long chatId);
    ChatEntity create(String name, Collection<UserEntity> users);
    void delete(ChatEntity chat);
    void deleteById(Long chatId);
    void updatePicture(ChatEntity chat, FileEntity picture);
    void updateChatName(ChatEntity chat, String chatName);
}
