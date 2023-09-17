package com.leopold.modules.file.dto.mapper;

import com.leopold.modules.file.dto.FileResponseDto;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.service.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Mapper(componentModel = "spring", implementationName = "FileResponseMapperImpl")
public abstract class FileResponseMapper implements Converter<FileEntity, FileResponseDto> {
    @Autowired
    FileService fileService;
    public FileResponseDto convert(FileEntity file) {
        FileResponseDto res = new FileResponseDto();
        res.setId(file.getFileId());
        res.setName(file.getName());
        res.setMimeType(file.getMimeType());
        res.setUrl(map(Optional.of(file)));
        return res;
    }

    @Named("getFileUrl")
    public String map(Optional<FileEntity> file) {
        return file.map(fileEntity -> fileService.composeUrl(fileEntity.getName())).orElse("");
    }


}
