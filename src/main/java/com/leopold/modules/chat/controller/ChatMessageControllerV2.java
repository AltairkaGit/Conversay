package com.leopold.modules.chat.controller;

import com.leopold.modules.chat.dto.*;
import com.leopold.modules.chat.dto.mapper.MessageMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageSeenService;
import com.leopold.modules.chat.service.MessageService;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.security.websocket.ChatAuthorizationSubscription;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ChatMessageControllerV2 {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private final FileService fileService;
    private final MessageService messageService;
    private final MessageSeenService messageSeenService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public ChatMessageControllerV2(
            ChatService chatService,
            UserService userService,
            MessageMapper messageMapper,
            FileService fileService,
            MessageService messageService,
            MessageSeenService messageSeenService,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageMapper = messageMapper;
        this.fileService = fileService;
        this.messageService = messageService;
        this.messageSeenService = messageSeenService;
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
        MessageResponseDto handledMessage = messageMapper.convert(messageEntity, me);
        simpMessagingTemplate.convertAndSend("/app/queue/chat/" + chatId + "/messages", handledMessage.toString());
    }

    @PutMapping("/api/v2/message")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    @Operation(summary = "update message if you are sender, you should send complete dto(as message should be)")
    public ResponseEntity<MessageResponseDto> updateMessage(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody UpdateMessageDto dto
    ) {
        Optional<MessageEntity> messageOptional = messageService.getMessageById(dto.getMessageId());
        if (messageOptional.isEmpty())
            throw new IllegalArgumentException("no message with this id");
        MessageEntity message = messageOptional.get();
        if (!Objects.equals(myId, message.getSender().getUserId()))
            throw new IllegalArgumentException("you are not the sender of the message");
        MessageEntity updatedMessage = messageService.updateMessage(
                message,
                dto.getContent(),
                messageService.getMessageById(dto.getReplyId()),
                dto.getFileUrls().stream().map(fileService::getFile).collect(Collectors.toList())
        );
        UserEntity me = userService.getUserById(myId);
        MessageResponseDto updatedMessageDto = messageMapper.convert(updatedMessage, me);
        return ResponseEntity.ok(updatedMessageDto);
    }

    @DeleteMapping("/api/v2/message")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    @Operation(summary = "update message if you are sender")
    public ResponseEntity<Void> deleteMessage(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody DeleteMessageDto dto
    ) {
        Optional<MessageEntity> messageOptional = messageService.getMessageById(dto.getMessageId());
        if (messageOptional.isEmpty())
            throw new IllegalArgumentException("no message with this id");
        MessageEntity message = messageOptional.get();
        if (!Objects.equals(myId, message.getSender().getUserId()))
            throw new IllegalArgumentException("you are not the sender of the message");
        messageService.deleteMessage(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v2/messages")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    @Operation(summary="read messages between start(like now) and end(like in the past)")
    public ResponseEntity<Void> readMessages(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody ReadChatMessagesDto dto
    ) {
        UserEntity me = userService.getUserById(myId);
        ChatEntity chat = chatService.getById(dto.getChatId());
        messageSeenService.readMessages(chat, me, dto.getStart(), dto.getEnd());
        return ResponseEntity.ok().build();
    }
}
