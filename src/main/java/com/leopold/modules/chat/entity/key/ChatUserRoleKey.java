package com.leopold.modules.chat.entity.key;

import com.leopold.roles.ChatRole;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatUserRoleKey implements Serializable {
    @Column(name="chat_id")
    private Long chatId;
    @Column(name="user_id")
    private Long userId;
    @Column(name="chat_role")
    private ChatRole role;

    public static ChatUserRoleKey ValueOf(Long chatId, Long userId, ChatRole role) {
        ChatUserRoleKey key = new ChatUserRoleKey();
        key.setChatId(chatId);
        key.setUserId(userId);
        key.setRole(role);
        return key;
    }
    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ChatRole getRole() {
        return role;
    }

    public void setRole(ChatRole role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUserRoleKey that = (ChatUserRoleKey) o;
        return Objects.equals(chatId, that.chatId) && Objects.equals(userId, that.userId) && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId, role);
    }
}
