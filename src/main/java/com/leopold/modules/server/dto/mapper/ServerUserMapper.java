package com.leopold.modules.server.dto.mapper;

import com.leopold.modules.server.dto.ServerUserProfileDto;
import com.leopold.modules.server.entity.ServerUserEntity;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = { UserProfileResponseMapper.class })
public interface ServerUserMapper {
    @Mapping(target = "profile", source = "serverUser.user")
    ServerUserProfileDto convert(ServerUserEntity serverUser);

    List<ServerUserProfileDto> convertList(Collection<ServerUserEntity> list);

    default Page<ServerUserProfileDto> convertPage(Page<ServerUserEntity> page) {
        List<ServerUserProfileDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
