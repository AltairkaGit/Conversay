package com.leopold.modules.chat.service;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface MessageService {
    MessageEntity createMessage(ChatEntity chat, UserEntity sender, MessageEntity messageFromDto);
    MessageEntity updateMessage(ChatEntity chat, UserEntity sender, MessageEntity messageFromDto);
    Optional<MessageEntity> getMessageById(Long messageId);
    Optional<MessageEntity> getLastMessage(ChatEntity chat);
    Page<MessageEntity> getAllChatMessages(ChatEntity chat, Pageable pageable);
    Page<MessageEntity> getAllChatMessagesBefore(ChatEntity chat, Timestamp origin, Pageable pageable);
    Set<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end);
    Page<MessageEntity> getAllSenderChatMessages(ChatEntity chat, UserEntity sender, Pageable pageable);
    Page<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end, Pageable pageable);
    Page<MessageEntity> getAllSenderChatMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end, Pageable pageable);
    void deleteMessage(MessageEntity message);
}
