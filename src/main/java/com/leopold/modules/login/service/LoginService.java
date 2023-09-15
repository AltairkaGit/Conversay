package com.leopold.modules.login.service;

import com.leopold.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;

@Transactional
public interface LoginService {
    String jwtLoginUsernamePassword(UserEntity user, String rawPassword);
    HttpHeaders setJwtCookieInHeaders(HttpHeaders old, String token);
    HttpHeaders removeJwtCookieFromHeaders(HttpHeaders old);
}
