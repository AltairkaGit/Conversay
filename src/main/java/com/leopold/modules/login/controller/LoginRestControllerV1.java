package com.leopold.modules.login.controller;

import com.leopold.modules.login.dto.LoginRequestDto;
import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.login.service.LoginService;
import com.leopold.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/login")
public class LoginRestControllerV1 {
    private final LoginService loginService;
    private final UserService userService;
    private final UserProfileResponseMapper userProfileResponseMapper;

    @Autowired
    public LoginRestControllerV1(LoginService loginService, UserService userService, UserProfileResponseMapper userProfileResponseMapper) {
        this.loginService = loginService;
        this.userService = userService;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    @PostMapping(value="")
    public ResponseEntity<UserProfileResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws CredentialException {
        UserEntity user = userService.getUserByUsername(loginRequestDto.getUsername());
        String token = loginService.jwtLoginUsernamePassword(user, loginRequestDto.getPassword());
        if (token.isEmpty()) throw new CredentialException();
        UserProfileResponseDto profile = userProfileResponseMapper.convert(user);
        HttpHeaders headers = new HttpHeaders();
        return  ResponseEntity.ok().headers(loginService.setJwtCookieInHeaders(headers, token)).body(profile);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<HttpStatus> logout() {
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(loginService.removeJwtCookieFromHeaders(headers)).build();
    }

    @ExceptionHandler({CredentialException.class, NoSuchElementException.class})
    public ResponseEntity<Object> handleWrongLogin(Exception ex) {
        return new ResponseEntity<>("Wrong login or password", HttpStatus.FORBIDDEN);
    }
}
