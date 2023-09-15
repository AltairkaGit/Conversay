package com.leopold.mapper;

import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.appRole.entity.AppRoleEntity;
import com.leopold.modules.file.entity.FileEntity;
import com.leopold.modules.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserProfileResponseMapperTest {
    private UserProfileResponseMapper userProfileResponseMapper;

    @Autowired
    public UserProfileResponseMapperTest(UserProfileResponseMapper userProfileResponseMapper) {
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    private static FileEntity file;
    private static UserEntity user;

    @BeforeAll
    static void beforeAll() {
        file = new FileEntity();
        file.setFileId(666L);
        file.setSize(1024);
        file.setUrl("/storage/file_123456.654321.jpg");
        file.setMimeType("image/jpeg");
        file.setName("file_123456.654321.jpg");

        AppRoleEntity admin = new AppRoleEntity();
        admin.setAppRoleId(1L);
        admin.setRole("Admin");
        List<AppRoleEntity> roles = new ArrayList<>();
        roles.add(admin);

        user = new UserEntity();
        user.setUserId(1L);
        user.setUsername("Altarka");
        user.setPassword("morgenshtern666");
        user.setGender(UserEntity.Gender.male);
        user.setEmail("shelby666zxc@gmail.com");
        user.setProfilePicture((file));
        user.setRoles(roles);
    }
    @Test
    void convert() {
        UserProfileResponseDto dto = userProfileResponseMapper.convert(user);
        assertNull(dto);
        assertEquals(dto.getUserId(), user.getUserId());
        assertEquals(dto.getGender(), user.getGender().toString());
        assertEquals(dto.getProfilePictureUrl(), file.getUrl());
        assertEquals(dto.getUsername(), user.getUsername());
    }

    @Test
    void convertList() {
        List<UserEntity> users = List.of(user);
        List<UserProfileResponseDto> dtos = userProfileResponseMapper.convertList(users);
        assertEquals(dtos.size(), users.size());
        Iterator<UserProfileResponseDto> dtoIt = dtos.iterator();
        Iterator<UserEntity> userIt = users.iterator();
        for (int i = 0; i < dtos.size(); i++) {
            UserProfileResponseDto dto = dtoIt.next();
            UserEntity user = userIt.next();
            assertNotNull(dto);
            assertEquals(dto.getUserId(), user.getUserId());
            assertEquals(dto.getGender(), user.getGender().toString());
            assertEquals(dto.getProfilePictureUrl(), file.getUrl());
            assertEquals(dto.getUsername(), user.getUsername());
        }
    }
}
