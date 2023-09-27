package com.leopold.modules.chat.service;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.exception.UserNotInTheChatException;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@Transactional
public interface ChatUserService {
    @Transactional(readOnly = true)
    Stream<UserEntity> getChatUsers(ChatEntity chat);
    boolean checkUserInChat(ChatEntity chat, UserEntity user);
    boolean checkUserInChat(Long chatId, Long userId);
    Page<ChatEntity> getUserChats(UserEntity user, Pageable pageable);
    Page<UserEntity> getChatUsers(ChatEntity chat, Pageable pageable);
    void addUser(ChatEntity chat, UserEntity user) throws Exception;
    void addUsers(ChatEntity chat, Collection<UserEntity> users);
    void removeUser(ChatEntity chat, UserEntity user) throws UserNotInTheChatException;
    void removeUsers(ChatEntity chat, Collection<UserEntity> users);
}
