package com.leopold.modules.login.service.impl;

import com.leopold.modules.login.dto.TokensResponseDto;
import com.leopold.modules.security.entity.RefreshTokenEntity;
import com.leopold.modules.security.repos.RefreshTokenRepository;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.login.service.LoginService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;
import java.util.Optional;


@Service
public class JwtLoginServiceImpl implements LoginService {
    @Value("${JWT_TOKEN_PREFIX}")
    private String tokenHeader;
    @Value("${JWT_TOKEN_EXPIRED}")
    private Integer tokenExpires;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    public JwtLoginServiceImpl(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public TokensResponseDto jwtLoginUsernamePassword(UserEntity user, String rawPassword) throws CredentialException {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new CredentialException("username or password is wrong");
        String refresh = jwtTokenProvider.createRefresh(user.getUserId(),user.getUsername(), user.getRoles());
        String access = jwtTokenProvider.createAccess(refresh);
        return new TokensResponseDto(access, refresh);
    }

    @Override
    public void logout(String access) throws AuthenticationException {
        String refresh = jwtTokenProvider.getRefreshFromAccess(access);
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefresh(refresh);
        if (refreshTokenEntity.isEmpty()) throw new AuthenticationException("token is not valid anymore");
        refreshTokenRepository.delete(refreshTokenEntity.get());
    }

    @Override
    public String refreshToken(String refresh) {
        return jwtTokenProvider.createAccess(refresh);
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

    private String bakeSetCookieHeader(Cookie cookie) {
        return String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly;",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(), cookie.getPath());
    }

}
