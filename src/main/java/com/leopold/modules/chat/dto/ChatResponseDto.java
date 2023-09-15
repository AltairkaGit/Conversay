package com.leopold.modules.chat.dto;

import com.leopold.modules.chat.entity.ChatEntity;

public class ChatResponseDto {
    private Long chatId;
    private String chatName;
    private String chatPictureUrl;
    private ChatEntity.ChatType chatType;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatPictureUrl() {
        return chatPictureUrl;
    }

    public void setChatPictureUrl(String chatPictureUrl) {
        this.chatPictureUrl = chatPictureUrl;
    }

    public ChatEntity.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatEntity.ChatType chatType) {
        this.chatType = chatType;
    }
}
