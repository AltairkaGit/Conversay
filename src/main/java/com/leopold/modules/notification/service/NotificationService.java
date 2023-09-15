package com.leopold.modules.notification.service;

import com.leopold.modules.chat.dto.MessageResponseDto;
import com.leopold.modules.chat.entity.ChatEntity;

public interface NotificationService {
    void nofifyNewMessage(ChatEntity chat, MessageResponseDto message);

}
