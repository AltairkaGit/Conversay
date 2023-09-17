package com.leopold.modules.file.service.impl;

import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.exception.FileIsTooGreatException;
import com.leopold.modules.file.repos.FileRepository;
import com.leopold.modules.file.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Value("${hostname}")
    private String hostname;
    @Value("${upload.path}")
    private String uploadPath;
    private final Long MAX_FILE_SIZE_BYTES = 1024 * 1024 * 35L;
    private final String DEFAULT_FILE_NAME = "file_";
    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileEntity getFile(String url) {
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash + 1 == url.length()) throw new NoSuchElementException("wrong url");
        String filename = url.substring(lastSlash + 1);
        Optional<FileEntity> file = fileRepository.findByName(filename);
        if (file.isEmpty()) throw new NoSuchElementException("file with this url is not present");
        return file.get();
    }

    @Override
    public FileEntity uploadFile(MultipartFile file) {
        if (file.isEmpty()) throw new MultipartException("empty file");
        long size = file.getSize();
        String origFileName = file.getOriginalFilename();
        if (origFileName == null) throw new MultipartException("no filename");
        int lastPointIdx = origFileName.lastIndexOf('.');
        if (size > MAX_FILE_SIZE_BYTES
                || lastPointIdx == -1
                || lastPointIdx + 1 == origFileName.length())
            throw new FileIsTooGreatException("file size greater than " + (MAX_FILE_SIZE_BYTES / 1024 / 1024) + "MB");
        String mimeType = file.getContentType();
        if (mimeType == null) mimeType = "";


        String ext = origFileName.substring(lastPointIdx + 1);
        String fileName = DEFAULT_FILE_NAME + UUID.randomUUID() + UUID.randomUUID() + '.' + ext;
        String filepath = uploadPath + "/" + fileName;
        File dest = new File(filepath);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new MultipartException("uploading exception, " + filepath);
        }
        FileEntity fileEntity = new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setMimeType(mimeType);
        fileEntity.setSize((int)size);
        fileRepository.saveAndFlush(fileEntity);

        return fileEntity;
    }

    @Override
    public void deleteFile(String url) {
        FileEntity file = getFile(url);
        fileRepository.deleteById(file.getFileId());
    }

    @Override
    public String composeUrl(String filename) {
        StringBuilder sb = new StringBuilder("http:/");
        if (!hostname.startsWith("/")) sb.append("/");
        sb.append(hostname);
        if (!uploadPath.startsWith("/")) sb.append("/");
        sb.append(uploadPath);
        sb.append("/");
        sb.append(filename);
        return sb.toString();
    }
}
