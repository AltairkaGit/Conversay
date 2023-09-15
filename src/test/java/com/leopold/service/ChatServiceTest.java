package com.leopold.service;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.service.FileService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatServiceTest {
    private final UserService userService;
    private final ChatService chatService;
    private final FileService fileService;

    @Autowired
    public ChatServiceTest(UserService userService, ChatService chatService, FileService fileService) {
        this.userService = userService;
        this.chatService = chatService;
        this.fileService = fileService;
    }

    @Test
    @Transactional
    void chatCRUD() throws Exception {
        //create
        String chatName = "Gay party";
        FileEntity pic = fileService.getFile("storage/g.party.jpg");
        ChatEntity chat = chatService.create(chatName, new ArrayList<>());
        assertEquals(chat.getChatName(), chatName);
        assertEquals(chat.getChatPicture(), pic);
        //read
        ChatEntity chatFetched = chatService.getById(chat.getChatId());
        assertEquals(chat, chatFetched);
        //update
        chatName = "Enclave";
        pic = fileService.getFile("http://localhost:8080/storage/enclave.jpg");
        chatService.updateChatName(chat, chatName);
        chatService.updatePicture(chat, pic);
        assertEquals(chat.getChatName(), chatName);
        assertEquals(chat.getChatPicture(), pic);

        Set<UserEntity> users = userService.getUsersByIds(List.of(1L,2L,3L,5L));
        chatService.addUsers(chat, users);
        Set<UserEntity> usersFetched = chatService.getChatUsers(chat, PageRequest.of(0, users.size())).toSet();
        assertEquals(users, usersFetched);

        Set<UserEntity> usersToRemove = userService.getUsersByIds(List.of(1L, 2L, 3L, 5L));
        chatService.removeUsers(chat, usersToRemove);

        //delete
        chatService.delete(chat);
    }
}
