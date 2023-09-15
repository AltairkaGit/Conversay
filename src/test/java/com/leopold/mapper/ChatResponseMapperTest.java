package com.leopold.mapper;

import com.leopold.modules.chat.dto.ChatResponseDto;
import com.leopold.modules.chat.dto.mapper.ChatResponseMapper;
import com.leopold.modules.chat.dto.mapper.MessageResponseMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.chat.service.MessageService;
import com.leopold.modules.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatResponseMapperTest {
    private final ChatService chatService;
    private final MessageService messageService;
    private final ChatResponseMapper chatResponseMapper;
    private final MessageResponseMapper messageResponseMapper;
    private final UserService userService;
    @Autowired
    public ChatResponseMapperTest(ChatService chatService, MessageService messageService, ChatResponseMapper chatResponseMapper, MessageResponseMapper messageResponseMapper, UserService userService) {
        this.chatService = chatService;
        this.messageService = messageService;
        this.chatResponseMapper = chatResponseMapper;
        this.messageResponseMapper = messageResponseMapper;
        this.userService = userService;
    }

    @Test
    void test() {
        UserEntity me = userService.getUserById(1L);
        ChatEntity chat = chatService.getById(1L);
        ChatResponseDto dto = chatResponseMapper.convert(chat, me);
        assertEquals(dto.getChatId(), chat.getChatId());
        assertEquals(dto.getChatName(), chat.getChatName());
    }
}
