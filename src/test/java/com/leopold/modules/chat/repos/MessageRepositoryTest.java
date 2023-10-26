package com.leopold.modules.chat.repos;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    private ChatEntity chat;
    private UserEntity me;
    private UserEntity companion;
    private MessageEntity message;
    private Timestamp origin;
    private long unreadMessagesByCompanion = 0;

    @BeforeEach
    void setUpEach() {
        chat = chatService.getById(1L);
        me = userService.getUserById(1L);
        companion = userService.getUserById(2L);
        origin = Timestamp.from(Instant.now());
        unreadMessagesByCompanion = messageRepository.countUnreadMessages(chat.getChatId(), companion.getUserId(), origin);

        message = new MessageEntity();
        message.setSender(me);
        message.setChat(chat);
        message.setContent("hello world!");
        message.setSendTimestamp(origin);
        message = messageRepository.save(message);
    }

    @Test
    void testGetLastMessage() {
        MessageEntity lastMessage = messageRepository.getLastMessage(chat.getChatId()).get();
        assertNotNull(lastMessage);
        assertEquals(message, lastMessage);
    }

    @Test
    void testCountUnreadMessages() {
        long updatedUnreadMessagesByCompanion = messageRepository.countUnreadMessages(chat.getChatId(), companion.getUserId(), origin);
        assertEquals(updatedUnreadMessagesByCompanion, unreadMessagesByCompanion + 1);
    }

    @Test
    void testGetUnreadMessages() {
        Timestamp now = Timestamp.from(Instant.now());
        Set<MessageEntity> unreadMessages = messageRepository.findUnreadMessagesBetween(chat.getChatId(), companion.getUserId(), now, origin);
        assertTrue(unreadMessages.contains(message));
    }

    @AfterEach
    void tearDownEach() {
        messageRepository.delete(message);
    }

}