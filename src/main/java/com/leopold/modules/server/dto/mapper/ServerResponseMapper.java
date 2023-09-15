package com.leopold.modules.server.dto.mapper;

import com.leopold.modules.server.dto.ServerResponseDto;
import com.leopold.modules.server.entity.ServerEntity;
import com.leopold.modules.file.dto.mapper.FileResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses={FileResponseMapper.class})
public interface ServerResponseMapper extends Converter<ServerEntity, ServerResponseDto> {
    @Mapping(target = "serverPictureUrl", source = "serverPicture", qualifiedByName = "getFileUrl")
    ServerResponseDto convert(ServerEntity server);
    List<ServerResponseDto> convertList(List<ServerEntity> servers);
    default Page<ServerResponseDto> convertPage(Page<ServerEntity> page) {
        List<ServerResponseDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

}
