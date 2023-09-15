package com.leopold.repos;

import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.repos.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @Transactional
    void getUser() {
        Optional<UserEntity> userEntity = userRepository.findById(1L);
        UserEntity user = userEntity.get();
        assertNotNull(user);
        assertEquals(user.getUsername(), "Altairka");
        assertEquals(user.getUserId(), 1L);
        assertEquals(user.getEmail(), "zxc666Shelby@gmail.com");
        assertEquals(user.getGender(), "male");

    }

}
