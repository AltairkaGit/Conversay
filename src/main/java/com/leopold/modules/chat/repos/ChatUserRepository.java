package com.leopold.modules.chat.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.ChatUserEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.entity.key.ChatUserKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, ChatUserKey> {
    Stream<ChatUserEntity> findAllByChat(ChatEntity chat);
    @Query( "SELECT cu.user FROM ChatUserEntity cu ")
    Page<UserEntity> findChatUsers(ChatEntity chat, Pageable pageable);

    @Query( "SELECT ch FROM ChatEntity ch " +
            "INNER JOIN ChatUserEntity chu " +
            "ON ch.chatId = chu.id.chatId " +
            "WHERE chu.user = :user " +
            "ORDER BY (SELECT MAX(mes.sendTimestamp) " +
            "          FROM MessageEntity mes " +
            "          WHERE mes.chat = ch )" +
            "DESC"
    )
    Page<ChatEntity> findUserChats(@Param("user") UserEntity user, Pageable pageable);
    Optional<ChatUserEntity> findByChatAndUser(ChatEntity chat, UserEntity user);
    Optional<ChatUserEntity> findTopByChat(ChatEntity chat);
    long countByChat(ChatEntity chat);
    default Stream<UserEntity> findChatUsers(ChatEntity chat) {
        return  findAllByChat(chat).map(ChatUserEntity::getUser);
    }
    void deleteAllByUserInAndChat(Collection<UserEntity> users, ChatEntity chat);
}