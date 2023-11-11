package com.leopold.modules.chat.websocket;

import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.security.websocket.SubscribeMiddleware;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ChatsSubeMiddleware implements SubscribeMiddleware {
    private final UserService userService;


    @Autowired
    public ChatsSubeMiddleware(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void subscribe(long userId) {
        UserEntity user = userService.getUserById(userId);
        Set<ChatEntity> chats = user.getMyChats();

    }
}
