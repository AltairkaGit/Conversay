package com.leopold.modules.server.dto;

public class CurrentConversationDto {
    String conversationId;
    ServerChannelDto channel;
    String serverId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ServerChannelDto getChannel() {
        return channel;
    }

    public void setChannel(ServerChannelDto channel) {
        this.channel = channel;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public CurrentConversationDto(String conversationId, ServerChannelDto channel, String serverId) {
        this.conversationId = conversationId;
        this.channel = channel;
        this.serverId = serverId;
    }
}
