package com.leopold.modules.auth.controller;

import com.leopold.modules.auth.dto.AuthRequestDto;
import com.leopold.modules.login.dto.TokensResponseDto;
import com.leopold.modules.auth.dto.mapper.AuthRequestMapper;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.auth.service.AuthService;
import com.leopold.modules.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("api/v2/auth")
public class AuthRestControllerV2 {
    private final AuthService authService;
    private final AuthRequestMapper authRequestMapper;
    private final LoginService loginService;
    @Autowired
    public AuthRestControllerV2(AuthService authService, AuthRequestMapper authRequestMapper, LoginService loginService) {
        this.authService = authService;
        this.authRequestMapper = authRequestMapper;
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<TokensResponseDto> createUser(@RequestBody AuthRequestDto authRequestDto) throws CredentialException {
        UserEntity registeringUser = authRequestMapper.convert(authRequestDto);
        UserEntity user = authService.registerUser(registeringUser);
        TokensResponseDto tokens = loginService.jwtLoginUsernamePassword(user, user.getPassword());
        return ResponseEntity.ok(tokens);
    }

}
