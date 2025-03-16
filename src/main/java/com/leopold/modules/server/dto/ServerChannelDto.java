package com.leopold.modules.server.dto;

import com.leopold.modules.server.entity.ServerChannelEntity;

import java.util.List;

public class ServerChannelDto {
    String channelId;
    String channelName;
    long chatId;
    ServerChannelEntity.ChannelType channelType;
    List<String> users;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public ServerChannelEntity.ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ServerChannelEntity.ChannelType channelType) {
        this.channelType = channelType;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
