package com.leopold.modules.notification.service;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.entity.ChatEntity;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface NotificationService {
    void nofifyNewMessage(ChatEntity chat, MessageResponseDto message);

}
