package com.leopold.modules.file.service;

import com.leopold.modules.file.entity.FileEntity;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Transactional
public interface FileService {
    FileEntity getFile(String url);
    FileEntity uploadFile(MultipartFile picture);
    void deleteFile(String url);
    private String encodeBase64Url(String rawUrl) {
        return Base64.getUrlEncoder().encodeToString(rawUrl.getBytes());
    }
    private String decodeBase64Url(String rawUrl) {
        return new String(Base64.getUrlDecoder().decode(rawUrl));
    }
}
