package com.leopold.modules.security.jwt.impl;

import com.leopold.modules.appRole.entity.AppRoleEntity;
import com.leopold.modules.security.entity.RefreshTokenEntity;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.security.repos.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String secret;
    private SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtTokenProviderImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostConstruct
    private void postConstruct() {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    @Override
    public String createAccess(String refresh) {
        Jws<Claims> refreshClaims = getClaims(refresh);
        Claims claims = Jwts.claims().setId(refreshClaims.getBody().getId());
        Optional<RefreshTokenEntity> entity = refreshTokenRepository.findByRefresh(refresh);
        claims.put("TokenId", entity.get().getTokenId());
        Date now = new Date();
        Date expired = new Date(now.getTime() + EXPIRED_ACCESS_MS);
        Date refreshExpired = getExpiration(getClaims(refresh));
        expired = expired.before(refreshExpired) ? expired : refreshExpired;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(key)
                .compact();
    }

    @Override
    public String createRefresh(Long userId, String username, List<AppRoleEntity> roles) {
        Claims claims = Jwts.claims().setSubject(username).setId(userId.toString());
        claims.put("AppRoles", mapAppRolesToStrings(roles));
        Date now = new Date();
        Date expired = new Date(now.getTime() + EXPIRED_REFRESH_MS);
        String refresh = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(key)
                .compact();
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefresh(refresh);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.saveAndFlush(refreshTokenEntity);
        return refresh;
    }

    @Override
    public boolean validateAccess(String access) {
        String refresh = getRefreshFromAccess(access).get().getRefresh();
        return validateRefresh(refresh);
    }

    @Override
    public boolean validateRefresh(String refresh) {
        String lookup = refresh.length() > 500 ? refresh.substring(0, 500) : refresh;
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefresh(lookup);
        if (refreshTokenEntity.isEmpty()) return false;
        Jws<Claims> claims = getClaims(refresh);
        long refreshTokenEntityUserId = refreshTokenEntity.get().getUserId();
        long tokenUserId = Long.parseLong(claims.getBody().getId());
        return refreshTokenEntityUserId == tokenUserId;
    }

    @Override
    public Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    @Override
    public Optional<RefreshTokenEntity> getRefreshFromAccess(String access) {
        Jws<Claims> claims = getClaims(access);
        return refreshTokenRepository.findByTokenId(Long.parseLong((String)claims.getBody().get("TokenId")));
    }

    @Override
    public String getUsernameFromRefresh(Jws<Claims> claims) {
        return claims.getBody().getSubject();
    }

    @Override
    public String getUsernameFromAccess(Jws<Claims> claims) {
        Jws<Claims> refreshClaims = getRefreshClaimsFromAccessClaims(claims);
        return getUsernameFromRefresh(refreshClaims);
    }

    @Override
    public String getAppRolesFromRefresh(Jws<Claims> claims) {
        return (String)claims.getBody().get("AppRoles");
    }

    @Override
    public String getAppRolesFromAccess(Jws<Claims> claims) {
        Jws<Claims> refreshClaims = getRefreshClaimsFromAccessClaims(claims);
        return getAppRolesFromRefresh(refreshClaims);
    }

    @Override
    public Long getUserId(Jws<Claims> claims) {
        String id = claims.getBody().getId();
        return Long.parseLong(id);
    }

    @Override
    public Date getExpiration(Jws<Claims> claims) {
        return claims.getBody().getExpiration();
    }

    private static List<String> mapAppRolesToStrings (List<AppRoleEntity> roles) {
        return roles.stream().map(AppRoleEntity::getRole).collect(Collectors.toList());
    }

    private Jws<Claims> getRefreshClaimsFromAccessClaims(Jws<Claims> claims) {
        String refresh = refreshTokenRepository.findByTokenId(Long.parseLong((String)claims.getBody().get("TokenId"))).get().getRefresh();
        return getClaims(refresh);
    }
}
