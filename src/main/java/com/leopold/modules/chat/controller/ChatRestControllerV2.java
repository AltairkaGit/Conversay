package com.leopold.modules.chat.controller;

import com.leopold.lib.page.PageDto;
import com.leopold.modules.chat.dto.ChatResponseDto;
import com.leopold.modules.chat.dto.CreateGroupChatRequestDto;
import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.dto.RemoveUsersFromChatRequestDto;
import com.leopold.modules.chat.dto.mapper.ChatResponseMapper;
import com.leopold.modules.chat.dto.mapper.MessageMapper;
import com.leopold.modules.chat.dto.mapper.RemoveUsersFromChatMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.exception.UserNotInTheChatException;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageService;
import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v2/chat")
public class ChatRestControllerV2 {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;
    private final ChatResponseMapper chatResponseMapper;
    private final MessageMapper messageMapper;
    private final UserProfileResponseMapper userProfileResponseMapper;
    private final RemoveUsersFromChatMapper removeUsersFromChatMapper;

    @Autowired
    public ChatRestControllerV2(
            ChatService chatService,
            UserService userService,
            MessageService messageService,
            ChatResponseMapper chatResponseMapper,
            MessageMapper messageMapper, UserProfileResponseMapper userProfileResponseMapper,
            RemoveUsersFromChatMapper removeUsersFromChatMapper) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
        this.chatResponseMapper = chatResponseMapper;
        this.messageMapper = messageMapper;
        this.userProfileResponseMapper = userProfileResponseMapper;
        this.removeUsersFromChatMapper = removeUsersFromChatMapper;
    }

    @Operation(summary = "Get a page of your chats")
    @GetMapping(value="")
    public ResponseEntity<PageDto<ChatResponseDto>> getChats(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<ChatEntity> chats = chatService.getUserChats(me, pageable);
        PageDto<ChatResponseDto> res = PageDto.of(chatResponseMapper.convertPage(chats, me));
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get a chat by id, you should be a participant")
    @GetMapping(value="/{chatId}")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    public ResponseEntity<ChatResponseDto> getChat(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId
    ) {
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(userId);
        ChatResponseDto res = chatResponseMapper.convert(chat, me);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "create a group chat")
    @PostMapping("/")
    public ResponseEntity<ChatResponseDto> createChat(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody CreateGroupChatRequestDto dto
    ) {
        Set<UserEntity> users = userService.getUsersByUsername(dto.getUsernames());
        UserEntity me = userService.getUserById(userId);
        ChatEntity chat = chatService.create(dto.getName(), me, users);
        ChatResponseDto res = chatResponseMapper.convert(chat, me);
        return ResponseEntity.ok(res);
    }

    //TODO: добавление ролей

    @DeleteMapping("/{chatId}")
    @Operation(summary = "leave a chat if you are a participant")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    public ResponseEntity<Void> leaveChat(
            @RequestAttribute("reqUserId") Long myId,
            @PathVariable Long chatId
    ) throws UserNotInTheChatException {
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(myId);
        chatService.removeUser(chat, me);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{chatId}/users")
    @Operation(summary = "kick users from a chat, you should be admin or moderator")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Admin.name()) || hasAuthority(T(com.leopold.roles.ChatRole).Moderator.name())")
    public ResponseEntity<Void> removeFromChat(
            @RequestAttribute("reqUserId") Long myId,
            @PathVariable Long chatId,
            @RequestBody RemoveUsersFromChatRequestDto ids
            ) throws UserNotInTheChatException {
        ChatEntity chat = chatService.getById(chatId);
        Set<UserEntity> users = removeUsersFromChatMapper.convert(ids);
        chatService.removeUsers(chat, users);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/{chatId}/messages")
    @Operation(summary = "get messages from the chat, you should be a participant")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    public ResponseEntity<Page<MessageResponseDto>> getChatMessages(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId,
            Pageable pageable
    ) {
        ChatEntity chat = chatService.getById(chatId);
        Page<MessageEntity> messages = messageService.getAllChatMessages(chat, pageable);
        Page<MessageResponseDto> res = messageMapper.convertPage(messages);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value="/{chatId}/users")
    @Operation(summary = "get users of the chat, you should be a participant")
    @PreAuthorize("hasAuthority(T(com.leopold.roles.ChatRole).Participant.name())")
    public ResponseEntity<Page<UserProfileResponseDto>> getChatUsers(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId,
            Pageable pageable
    ) {
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(userId);
        Page<UserEntity> users = chatService.getChatUsers(chat, pageable);
        Page<UserProfileResponseDto> res = userProfileResponseMapper.convertPage(users);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
