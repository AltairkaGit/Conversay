package com.leopold.modules.server.dto;

import com.leopold.modules.server.entity.ServerChannelEntity;

public class CreateChannelDto {
    String channelName;
    ServerChannelEntity.ChannelType channelType;

    public String getChannelName() {
        return channelName;
    }

    public ServerChannelEntity.ChannelType getChannelType() {
        return channelType;
    }
}
