package com.leopold.modules.server.service;

import com.leopold.modules.server.entity.ServerChannelEntity;
import com.leopold.modules.server.entity.ServerEntity;

public interface ServerChannelService {
    ServerChannelEntity createChannel(ServerEntity server, String channelName, ServerChannelEntity.ChannelType channelType);
}
