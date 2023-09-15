package com.leopold.service;

import com.leopold.modules.auth.service.AuthService;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.security.auth.login.CredentialException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    private final UserService userService;
    private final AuthService authService;
    @Autowired
    public UserServiceTest(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Test
    @Transactional
    void userCRUD() throws CredentialException {
        //create
        String username = "ShelbyGeorge",
                email = "altairka@gmail.com",
                password= "zxc666shelby";
        UserEntity.Gender gender = UserEntity.Gender.female;
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setGender(gender);
        user = authService.registerUser(user);
        Long userId = user.getUserId();
        //read
        user = userService.getUserById(userId);
        assertEquals(userId, user.getUserId());
        assertEquals(username, user.getUsername());
        assertEquals(gender, user.getGender());
        assertEquals(email, user.getEmail());
        //update
        email = "altairka@yandex.ru";
        username = "morgen727";
        password = "zxc42342shelby";
        userService.updateUsername(user, username);
        userService.updateEmail(user, email);
        userService.updatePassword(user, password);
        assertEquals(userId, user.getUserId());
        assertEquals(username, user.getUsername());
        assertEquals(gender, user.getGender());
        assertEquals(email, user.getEmail());
        //delete
        userService.deleteUser(user);
    }

}
