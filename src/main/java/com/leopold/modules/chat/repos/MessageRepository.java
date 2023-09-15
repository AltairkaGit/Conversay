package com.leopold.modules.chat.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Optional<MessageEntity> findTopByChatOrderBySendTimestampDesc(ChatEntity chat);
    Page<MessageEntity> findAllByChat(ChatEntity chat, Pageable pageable);
    Page<MessageEntity> findAllByChatAndSender(ChatEntity chat, UserEntity sender, Pageable pageable);
    Page<MessageEntity> findAllByChatAndSendTimestampBetween(ChatEntity chat, Timestamp start, Timestamp end, Pageable pageable);
    Page<MessageEntity> findAllByChatAndSenderAndSendTimestampBetween(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end, Pageable pageable);
}
