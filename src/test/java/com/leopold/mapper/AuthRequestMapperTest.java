package com.leopold.mapper;

import com.leopold.modules.auth.dto.AuthRequestDto;
import com.leopold.modules.auth.dto.mapper.AuthRequestMapper;
import com.leopold.modules.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthRequestMapperTest {
    private AuthRequestMapper authRequestMapper;

    @Autowired
    public AuthRequestMapperTest(AuthRequestMapper authRequestMapper) {
        this.authRequestMapper = authRequestMapper;
    }

    @Test
    void convert() {
        AuthRequestDto authRequestDto = new AuthRequestDto();

        authRequestDto.setUsername("Altairka");
        authRequestDto.setEmail("altairka@shelby.zxc");
        authRequestDto.setPassword("zxcshelby666");
        authRequestDto.setGender("male");

        UserEntity user = authRequestMapper.convert(authRequestDto);
        assertEquals(authRequestDto.getUsername(), user.getUsername());
        assertEquals(authRequestDto.getEmail(), user.getEmail());
        assertEquals(authRequestDto.getPassword(), user.getPassword());
        assertEquals(authRequestDto.getGender(), user.getGender());
    }
}
