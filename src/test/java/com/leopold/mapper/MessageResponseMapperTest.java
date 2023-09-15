package com.leopold.mapper;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.chat.dto.mapper.MessageResponseMapper;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.chat.entity.MessageEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MessageResponseMapperTest {
    private final MessageResponseMapper messageResponseMapper;
    private final UserProfileResponseMapper userProfileResponseMapper;
    private static ChatEntity chat;
    private static MessageEntity message;
    private static MessageEntity reply;
    private static UserEntity friend;
    private static UserEntity me;
    @Autowired
    public MessageResponseMapperTest(MessageResponseMapper messageResponseMapper, UserProfileResponseMapper userProfileResponseMapper) {
        this.messageResponseMapper = messageResponseMapper;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    @BeforeAll
    static void beforeAll() {
        friend = new UserEntity();
        friend.setUserId(3L);
        friend.setUsername("max");

        me = new UserEntity();
        me.setUserId(1L);
        me.setUsername("altairka");

        chat = new ChatEntity();
        chat.setChatId(82L);
        chat.setChatName("testChat");

        reply = new MessageEntity();
        reply.setMessageId(7L);
        reply.setChat(chat);
        reply.setContent("В dlss есть разные настройки, друг");
        reply.setSender(friend);
        reply.setSendTimestamp(Timestamp.valueOf("2023-02-17 13:23:05"));

        message = new MessageEntity();
        message.setMessageId(8L);
        message.setChat(chat);
        message.setContent("Ну я тебе по существу говорю, а не про настройки");
        message.setSender(me);
        message.setReply(reply);
        message.setSendTimestamp(Timestamp.valueOf("2023-02-17 13:24:37"));
    }

    @Test
    void convert() {
        UserProfileResponseDto meDto = userProfileResponseMapper.convert(me);
        MessageResponseDto dto = messageResponseMapper.convert(message);
        assertEquals(dto.getMessageId(), message.getMessageId());
        assertEquals(dto.getChatId(), chat.getChatId());
        assertEquals(dto.getContent(), message.getContent());
        assertEquals(dto.getReplyId(), message.getReply().getMessageId());
        assertEquals(dto.getReplyContent(), message.getReply().getContent());
        assertEquals(dto.getSendTimestamp(), message.getSendTimestamp());
        assertEquals(dto.getSenderId(), meDto.getUserId());
    }
}
