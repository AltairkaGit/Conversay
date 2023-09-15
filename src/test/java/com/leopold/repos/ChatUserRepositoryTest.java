package com.leopold.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.repos.ChatRepository;
import com.leopold.modules.chat.repos.ChatUserRepository;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.repos.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ChatUserRepositoryTest {
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    @Autowired
    public ChatUserRepositoryTest(ChatUserRepository chatUserRepository, UserRepository userRepository, ChatRepository chatRepository) {
        this.chatUserRepository = chatUserRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    @Test
    void getUserChats() {
        Pageable pageable = PageRequest.of(0, 100);
        UserEntity user = userRepository.findByUsername("Altairka").get();
        List<ChatEntity> chats = chatUserRepository.findUserChats(user, pageable).getContent();

        assertEquals(2, chats.size());
    }

    @Test
    void getChatUsers() {
        Pageable pageable = PageRequest.of(0, 100);
        ChatEntity chat = chatRepository.findById(2L).get();
        List<UserEntity> users = chatUserRepository.findChatUsers(chat, pageable).getContent();

        assertEquals(2, users.size());
    }
}
