package com.leopold.modules.login.service.impl;

import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.login.service.LoginService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class JwtLoginServiceImpl implements LoginService {
    @Value("${JWT_TOKEN_PREFIX}")
    private String tokenHeader;
    @Value("${JWT_TOKEN_EXPIRED}")
    private Integer tokenExpires;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public JwtLoginServiceImpl(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String jwtLoginUsernamePassword(UserEntity user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) return "";
        return jwtTokenProvider.createToken(user.getUserId(),user.getUsername(), user.getRoles());
    }


    @Override
    public HttpHeaders setJwtCookieInHeaders(HttpHeaders old, String token) {
        HttpHeaders headers = new HttpHeaders(old);
        Cookie cookie = new Cookie(tokenHeader, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(tokenExpires);
        cookie.setPath("/");
        String setCookieHeader =bakeSetCookieHeader(cookie);
        headers.add("Set-Cookie", setCookieHeader);
        return headers;
    }

    @Override
    public HttpHeaders removeJwtCookieFromHeaders(HttpHeaders old) {
        HttpHeaders headers = new HttpHeaders(old);
        Cookie cookie = new Cookie(tokenHeader, "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        String setCookieHeader =bakeSetCookieHeader(cookie);
        headers.add("Set-Cookie", setCookieHeader);
        return headers;
    }

    private String bakeSetCookieHeader(Cookie cookie) {
        return String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly;",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(), cookie.getPath());
    }

}
