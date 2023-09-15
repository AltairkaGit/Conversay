package com.leopold.mapper;

import com.leopold.modules.file.dto.FileResponseDto;
import com.leopold.modules.file.dto.mapper.FileResponseMapper;
import com.leopold.modules.file.entity.FileEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileResponseMapperTest {
    @Autowired
    private FileResponseMapper fileResponseMapper;

    @Test
    void convertion() {
        FileEntity file = new FileEntity();
        file.setFileId(666L);
        file.setSize(1024);
        file.setUrl("storage/file_123456.654321.jpg");
        file.setMimeType("image/jpeg");
        file.setName("file_123456.654321.jpg");

        FileResponseDto dto = fileResponseMapper.convert(file);

        assertEquals(file.getFileId(), dto.getId());
        assertEquals(file.getName(), dto.getName());
        assertEquals(file.getUrl(), dto.getUrl());
        assertEquals(file.getMimeType(), dto.getMimeType());
    }
}
