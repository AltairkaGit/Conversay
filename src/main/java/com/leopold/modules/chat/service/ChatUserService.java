package com.leopold.modules.chat.service;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.ChatUserEntity;
import com.leopold.modules.chat.exception.UserAlreadyInTheChatException;
import com.leopold.modules.chat.exception.UserNotInTheChatException;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.roles.ChatRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Transactional
public interface ChatUserService {
    @Transactional(readOnly = true)
    Stream<UserEntity> getChatUsers(ChatEntity chat);
    @Transactional(readOnly = true)
    Set<Long> getAllChatUserIds(ChatEntity chat);

    @Transactional(readOnly = true)
    Stream<Long> streamAllChatUserIds(ChatEntity chat);
    @Transactional(readOnly = true)
    Page<ChatEntity> getUserChats(UserEntity user, Pageable pageable);
    @Transactional(readOnly = true)
    Page<UserEntity> getChatUsers(ChatEntity chat, Pageable pageable);
    Optional<ChatEntity> getDirectByUsers(UserEntity u1, UserEntity u2);
    void addChatModerator(ChatEntity chat, UserEntity user);
    void removeChatModerator(ChatEntity chat, UserEntity user);

    UserEntity getChatAdmin(ChatEntity chat);
    Set<UserEntity> getChatModerators(ChatEntity chat);
    void setNewChatAdminDeleteOld(ChatEntity chat, UserEntity user);

    List<ChatRole> getUserChatRoles(ChatEntity chat, UserEntity user);

    @Transactional(readOnly = true)
    long countChatUsers(ChatEntity chat);
    void addUser(ChatEntity chat, UserEntity user) throws UserAlreadyInTheChatException;
    void addUsers(ChatEntity chat, Collection<UserEntity> users);
    void removeUser(ChatEntity chat, UserEntity user) throws UserNotInTheChatException;
    void removeUsers(ChatEntity chat, Collection<UserEntity> users);
}
