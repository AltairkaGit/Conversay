package com.leopold.modules.chat.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.ChatUserEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.entity.key.ChatUserKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, ChatUserKey> {

    Stream<ChatUserEntity> findAllByChat(ChatEntity chat);
    Page<ChatUserEntity> findAllByChat(ChatEntity chat, Pageable pageable);
    Page<ChatUserEntity> findAllByUser(UserEntity user, Pageable pageable);
    Optional<ChatUserEntity> findByChatAndUser(ChatEntity chat, UserEntity user);
    Long countByChat(ChatEntity chat);
    default Page<UserEntity> findChatUsers(ChatEntity chat, Pageable pageable) {
        Page<ChatUserEntity> users = findAllByChat(chat, pageable);
        return users.map(ChatUserEntity::getUser);
    }
    default Stream<UserEntity> findChatUsers(ChatEntity chat) {
        return  findAllByChat(chat)
                .map(ChatUserEntity::getUser);
    }
    default Page<ChatEntity> findUserChats(UserEntity user, Pageable pageable) {
        Page<ChatUserEntity> chats = findAllByUser(user, pageable);
        return chats.map(ChatUserEntity::getChat);
    }
}