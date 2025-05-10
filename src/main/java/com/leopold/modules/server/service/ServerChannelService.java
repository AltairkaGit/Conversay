package com.leopold.modules.server.service;

import com.leopold.modules.server.entity.ServerChannelEntity;
import com.leopold.modules.server.entity.ServerEntity;

import java.util.Optional;

public interface ServerChannelService {
    ServerChannelEntity createChannel(ServerEntity server, String channelName, ServerChannelEntity.ChannelType channelType);
    ServerChannelEntity getChannelById(String channelId);
    Optional<String> getServerIdByChannel(String channelId);
}
