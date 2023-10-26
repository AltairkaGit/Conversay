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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;

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

    @PutMapping("/api/v2/message")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    public ResponseEntity<MessageResponseDto> updateMessage(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody MessageResponseDto message
    ) {
        if (!Objects.equals(myId, message.getSenderId()))
            throw new IllegalArgumentException("you are not the sender of the message");
        UserEntity me = userService.getUserById(myId);
        ChatEntity chat = chatService.getById(message.getChatId());
        MessageEntity updatedMessage = messageService.updateMessage(chat, me, messageMapper.convert(message));
        MessageResponseDto updatedMessageDto = messageMapper.convert(updatedMessage);
        return ResponseEntity.ok(updatedMessageDto);
    }
}
