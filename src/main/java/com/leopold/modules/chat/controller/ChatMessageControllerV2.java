package com.leopold.modules.chat.controller;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.dto.MessageWebsocketDto;
import com.leopold.modules.chat.dto.mapper.MessageMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageService;
import com.leopold.modules.security.websocket.ChatAuthorizationSubscription;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageControllerV2 {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public ChatMessageControllerV2(
            ChatService chatService,
            UserService userService,
            MessageMapper messageMapper,
            MessageService messageService,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageMapper = messageMapper;
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @ChatAuthorizationSubscription
    @SubscribeMapping("/queue/chat/{chatId}/messages")
    public void subscribeChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("chatId") String chatIdParam
    ) {
    }

    @ChatAuthorizationSubscription
    @MessageMapping("/chat/{chatId}")
    public void sendMessage(
        SimpMessageHeaderAccessor accessor,
        @DestinationVariable("chatId") String chatIdParam,
        MessageWebsocketDto message
    ) {
        Long userId = (Long)accessor.getSessionAttributes().get("userId");
        Long chatId = Long.valueOf(chatIdParam);
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(userId);

        MessageEntity messageFromDto = messageMapper.convert(message);
        MessageEntity messageEntity = messageService.createMessage(chat, me, messageFromDto);
        MessageResponseDto handledMessage = messageMapper.convert(messageEntity);
        simpMessagingTemplate.convertAndSend("/app/queue/chat/" + chatId + "/messages", handledMessage.toString());
    }
}
