package com.leopold.modules.login.service.impl;

import com.leopold.modules.login.dto.TokensResponseDto;
import com.leopold.modules.security.entity.RefreshTokenEntity;
import com.leopold.modules.security.repos.RefreshTokenRepository;
import com.leopold.modules.user.entity.UserEntity;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.login.service.LoginService;
import com.leopold.modules.user.service.UserService;
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
    private final UserService userService;
    @Autowired
    public JwtLoginServiceImpl(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public TokensResponseDto jwtLoginUsernamePassword(UserEntity user, String rawPassword) throws CredentialException {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new CredentialException("username or password is wrong");
        return generateTokens(user);
    }

    @Override
    public void logoutSession(String access) throws AuthenticationException {
        Optional<RefreshTokenEntity> refresh = jwtTokenProvider.getRefreshFromAccess(access);
        if (refresh.isEmpty()) throw new AuthenticationException("token is not valid anymore");
        refreshTokenRepository.delete(refresh.get());
    }

    @Override
    public void logoutAllSessions(String access) throws AuthenticationException {
        Optional<RefreshTokenEntity> refresh = jwtTokenProvider.getRefreshFromAccess(access);
        if (refresh.isEmpty()) throw new AuthenticationException("token is not valid anymore");
        refreshTokenRepository.deleteAllByUserId(refresh.get().getUserId());
    }

    @Override
    public TokensResponseDto refreshToken(String refresh) throws CredentialException {
        Optional<RefreshTokenEntity> token = refreshTokenRepository.findByRefresh(refresh);
        if (token.isEmpty()) throw new CredentialException("login again, please");
        UserEntity user = userService.getUserById(jwtTokenProvider.getUserId(jwtTokenProvider.getClaims(refresh)));
        TokensResponseDto tokens = generateTokens(user);
        updateRefresh(token.get(), tokens.getRefresh());
        return tokens;
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

    private TokensResponseDto generateTokens(UserEntity user) {
        String refreshed = jwtTokenProvider.createRefresh(user.getUserId(), user.getRoles());
        String access = jwtTokenProvider.createAccess(refreshed);
        return new TokensResponseDto(access, refreshed);
    }

    private void updateRefresh(RefreshTokenEntity token, String refresh) {
        token.setRefresh(refresh);
        refreshTokenRepository.saveAndFlush(token);
    }

}
