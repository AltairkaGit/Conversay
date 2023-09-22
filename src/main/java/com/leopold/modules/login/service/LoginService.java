package com.leopold.modules.login.service;

import com.leopold.modules.login.dto.TokensResponseDto;
import com.leopold.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;

@Transactional
public interface LoginService {
    TokensResponseDto jwtLoginUsernamePassword(UserEntity user, String rawPassword) throws CredentialException;
    void logoutSession(String access) throws AuthenticationException;
    void logoutAllSessions(String access) throws AuthenticationException;

    String refreshToken(String refresh);
    HttpHeaders setJwtCookieInHeaders(HttpHeaders old, String token);
}
