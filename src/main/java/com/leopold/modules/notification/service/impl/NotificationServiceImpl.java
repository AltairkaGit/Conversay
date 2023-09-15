package com.leopold.modules.notification.service.impl;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.entity.ChatEntity;
import com.leopold.modules.notification.service.NotificationService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void nofifyNewMessage(ChatEntity chat, MessageResponseDto message) {

    }
}
