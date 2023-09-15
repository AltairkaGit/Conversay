package com.leopold.modules.file.dto.mapper;

import com.leopold.modules.file.dto.FileResponseDto;
import com.leopold.modules.file.entity.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Mapper(componentModel = "spring", implementationName = "FileResponseMapperImpl")
public interface FileResponseMapper extends Converter<FileEntity, FileResponseDto> {

    @Mapping(source = "fileId", target = "id")
    FileResponseDto convert(FileEntity file);

    @Named("getFileUrl")
    default String map(Optional<FileEntity> file) {
        if (file.isEmpty()) return "";
        return file.get().getUrl();
    }
}
