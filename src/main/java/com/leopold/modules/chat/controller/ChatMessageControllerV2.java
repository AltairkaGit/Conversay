package com.leopold.modules.chat.controller;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.dto.mapper.MessageResponseMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageService;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;

@Controller
public class ChatMessageControllerV2 {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageResponseMapper messageResponseMapper;
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public ChatMessageControllerV2(
            ChatService chatService,
            UserService userService,
            MessageResponseMapper messageResponseMapper,
            MessageService messageService,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageResponseMapper = messageResponseMapper;
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat/{chatId}/{userId}")
    public MessageResponseDto sendMessage(
            @DestinationVariable("chatId") String chatIdParam,
            @DestinationVariable("userId") String userIdParam,
            MessageResponseDto message
    ) {
        Long userId = Long.valueOf(userIdParam);
        Long chatId = Long.valueOf(chatIdParam);
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(userId);

        MessageEntity messageEntity = messageResponseMapper.convert(message);
        MessageResponseDto handledMessage = messageResponseMapper.convert(messageService.createMessage(chat, me, messageEntity));

        simpMessagingTemplate.convertAndSend("/chat/" + chatId + "/messages", handledMessage);
        return handledMessage;
    }
}
