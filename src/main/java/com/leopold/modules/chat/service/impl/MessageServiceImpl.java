package com.leopold.modules.chat.service.impl;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.repos.MessageRepository;
import com.leopold.modules.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @Override
    public MessageEntity createMessage(ChatEntity chat, UserEntity sender, MessageEntity messageFromDto) {
        messageFromDto.setSender(sender);
        messageFromDto.setChat(chat);
        return messageRepository.save(messageFromDto);
    }

    @Override
    public MessageEntity updateMessage(ChatEntity chat, UserEntity sender, MessageEntity messageFromDto) {
        messageFromDto.setSender(sender);
        messageFromDto.setChat(chat);
        return messageRepository.save(messageFromDto);
    }

    @Override
    public void deleteMessage(MessageEntity message) {
        messageRepository.delete(message);
    }

    @Override
    public Optional<MessageEntity> getMessageById(Long messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public Optional<MessageEntity> getLastMessage(ChatEntity chat) {
        return messageRepository.findTopByChatOrderBySendTimestampDesc(chat);
    }

    @Override
    public Page<MessageEntity> getAllChatMessages(ChatEntity chat, Pageable pageable) {
        return messageRepository.findAllByChat(chat, pageable);
    }

    @Override
    public Page<MessageEntity> getAllSenderChatMessages(ChatEntity chat, UserEntity sender, Pageable pageable) {
        return messageRepository.findAllByChatAndSender(chat, sender, pageable);
    }

    @Override
    public Page<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end, Pageable pageable) {
        return messageRepository.findAllByChatAndSendTimestampBetween(chat, start, end, pageable);
    }

    @Override
    public Page<MessageEntity> getAllSenderChatMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end, Pageable pageable) {
        return messageRepository.findAllByChatAndSenderAndSendTimestampBetween(chat, sender, start, end, pageable);
    }
}
