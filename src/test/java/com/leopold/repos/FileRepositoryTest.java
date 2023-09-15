package com.leopold.repos;

import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.file.repos.FileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class FileRepositoryTest {
    private final FileRepository fileRepository;

    @Autowired
    public FileRepositoryTest(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Test
    void getFile() {
        Optional<FileEntity> fileEntity = fileRepository.findById(1L);
        FileEntity file = fileEntity.get();
        assertNotNull(file);
        assertEquals(file.getFileId(), 1L);
        assertEquals(file.getSize(), 201567);
        assertEquals(file.getName(), "altairka.jpg");
        assertEquals(file.getMimeType(), "image/jpeg");
        assertEquals(file.getUrl(), "storage/altairka.jpg");
    }
}
