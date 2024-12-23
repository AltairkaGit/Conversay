package com.leopold.modules.server.dto.mapper;

import com.leopold.modules.server.dto.ServerChannelDto;
import com.leopold.modules.server.entity.ServerChannelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ServerChannelMapper {
    @Mapping(target = "chatId", source = "channel.chat.chatId")
    ServerChannelDto convert(ServerChannelEntity channel);
    @Named("getChannelsDto")
    List<ServerChannelDto> convertList(List<ServerChannelEntity> servers);
}
