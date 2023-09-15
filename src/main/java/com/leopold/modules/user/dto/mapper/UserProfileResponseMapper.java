package com.leopold.modules.user.dto.mapper;

import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.user.entity.UserEntity;
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
public interface UserProfileResponseMapper extends Converter<UserEntity, UserProfileResponseDto> {

    @Mapping(target = "profilePictureUrl", source = "profilePicture", qualifiedByName = "getFileUrl")
    UserProfileResponseDto convert(UserEntity user);

    List<UserProfileResponseDto> convertList(List<UserEntity> users);
    default Page<UserProfileResponseDto> convertPage(Page<UserEntity> page) {
        List<UserProfileResponseDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
