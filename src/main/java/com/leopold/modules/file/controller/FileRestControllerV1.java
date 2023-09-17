package com.leopold.modules.file.controller;

import com.leopold.modules.file.dto.FileResponseDto;
import com.leopold.modules.file.dto.mapper.FileResponseMapper;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
public class FileRestControllerV1 {
    private final FileResponseMapper fileResponseMapper;
    private final FileService fileService;
    @Autowired
    public FileRestControllerV1(FileResponseMapper fileResponseMapper, FileService fileService) {
        this.fileResponseMapper = fileResponseMapper;
        this.fileService = fileService;
    }

    @PostMapping("")
    public ResponseEntity<FileResponseDto> upload(
            @RequestParam("file") MultipartFile file
    ) {
        FileEntity entity = fileService.uploadFile(file);
        FileResponseDto dto = fileResponseMapper.convert(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
