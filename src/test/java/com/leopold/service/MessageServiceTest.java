package com.leopold.service;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.dto.mapper.MessageResponseMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageServiceTest {
    private final MessageService messageService;
    private final MessageResponseMapper messageResponseMapper;
    private final UserService userService;
    private final ChatService chatService;
    @Autowired
    public MessageServiceTest(MessageService messageService, MessageResponseMapper messageResponseMapper, UserService userService, ChatService chatService) {
        this.messageService = messageService;
        this.messageResponseMapper = messageResponseMapper;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Test
    @Transactional
    void messageCRUD() {
        UserEntity user = userService.getUserByUsername("Altairka");
        ChatEntity chat = chatService.getById(1L);
        MessageResponseDto messageResponseDto = new MessageResponseDto();
        messageResponseDto.setContent("test");
        messageResponseDto.setSendTimestamp(Timestamp.from(Instant.now()));
        MessageEntity messageFromDto = new MessageEntity();
        messageFromDto = messageResponseMapper.convert(messageResponseDto);
        //create
        MessageEntity newMessage = messageService.createMessage(chat, user, messageFromDto);
        //read
        MessageEntity lastMessage = messageService.getLastMessage(chat).get();
        assertEquals(newMessage.getMessageId(), lastMessage.getMessageId());
        assertEquals(newMessage.getContent(), lastMessage.getContent());
        assertEquals(newMessage.getChat(), lastMessage.getChat());
        assertEquals(newMessage.getSender(), lastMessage.getSender());
        assertEquals(newMessage.getSender(), lastMessage.getSender());
        assertEquals(newMessage.getSendTimestamp(), lastMessage.getSendTimestamp());
        //update
        messageResponseDto.setContent("updating content");
        messageResponseDto.setSendTimestamp(Timestamp.from(Instant.now()));
        newMessage = messageService.updateMessage(chat, user, messageResponseMapper.convert(messageResponseDto));
        assertNotEquals(newMessage.getContent(), lastMessage.getContent());
        assertNotEquals(newMessage.getSendTimestamp(), lastMessage.getSendTimestamp());
        //delete
        messageService.deleteMessage(newMessage);
    }
}
