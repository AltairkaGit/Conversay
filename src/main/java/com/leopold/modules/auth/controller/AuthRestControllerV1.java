package com.leopold.modules.auth.controller;

import com.leopold.modules.auth.dto.AuthRequestDto;
import com.leopold.modules.user.dto.UserProfileResponseDto;
import com.leopold.modules.auth.dto.mapper.AuthRequestMapper;
import com.leopold.modules.user.dto.mapper.UserProfileResponseMapper;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.auth.service.AuthService;
import com.leopold.modules.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("api/v1/auth")
public class AuthRestControllerV1 {
    private final AuthService authService;
    private final AuthRequestMapper authRequestMapper;
    private final UserProfileResponseMapper userProfileResponseMapper;
    private final LoginService loginService;
    @Autowired
    public AuthRestControllerV1(AuthService authService, AuthRequestMapper authRequestMapper, UserProfileResponseMapper userProfileResponseMapper, LoginService loginService) {
        this.authService = authService;
        this.authRequestMapper = authRequestMapper;
        this.userProfileResponseMapper = userProfileResponseMapper;
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<UserProfileResponseDto> createUser(@RequestBody AuthRequestDto authRequestDto) throws CredentialException {
        UserEntity registeringUser = authRequestMapper.convert(authRequestDto);
        UserEntity user = authService.registerUser(registeringUser);
        UserProfileResponseDto profile = userProfileResponseMapper.convert((user));
        String token = loginService.jwtLoginUsernamePassword(user, user.getPassword());
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(loginService.setJwtCookieInHeaders(headers, token)).body(profile);
    }

}
