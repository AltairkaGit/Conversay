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
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;
    private final UserService userService;
    private final MessageResponseMapper messageResponseMapper;
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public ChatMessageControllerV2(
            JwtTokenProvider jwtTokenProvider,
            ChatService chatService,
            UserService userService,
            MessageResponseMapper messageResponseMapper,
            MessageService messageService,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatService = chatService;
        this.userService = userService;
        this.messageResponseMapper = messageResponseMapper;
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/chat/{chatId}")
    //@PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRoles).Participant.name())")
    public MessageResponseDto sendMessage(
            @DestinationVariable("chatId") String chatIdParam,
            @CookieValue("Authorization") String access,
            MessageResponseDto message
    ) {
        if (!jwtTokenProvider.validateAccess(access)) throw new SecurityException("token is not valid");
        Long userId = jwtTokenProvider.getUserId(jwtTokenProvider.getClaims(access));
        Long chatId = Long.valueOf(chatIdParam);
        if (!chatService.checkUserInChat(chatId, userId)) throw new SecurityException("you are not in the chat");
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(userId);

        MessageEntity messageEntity = messageResponseMapper.convert(message);
        MessageResponseDto handledMessage = messageResponseMapper.convert(messageService.createMessage(chat, me, messageEntity));

        simpMessagingTemplate.convertAndSend("/chat/" + chatId + "/messages", handledMessage);
        return handledMessage;
    }
}
