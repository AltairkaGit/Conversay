package com.leopold.modules.security.jwt;

import com.leopold.modules.appRole.entity.AppRoleEntity;
import com.leopold.modules.security.entity.RefreshTokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
public interface JwtTokenProvider {
    long EXPIRED_ACCESS_MS = 30 * 24 * 60 * 1000;
    long EXPIRED_REFRESH_MS = 365 * 24 * 60 * 1000;
    String generateAccess(String refresh);
    String generateRefresh(Long userId, List<AppRoleEntity> roles);
    RefreshTokenEntity createRefresh(Long userId, String refresh);
    boolean validateAccess(String access);
    boolean validateRefresh(String refresh);
    Jws<Claims> getClaims(String token);
    Optional<RefreshTokenEntity> getRefreshFromAccess(String access);
    Date getExpiration(Jws<Claims> claims);
    Long getUserId(Jws<Claims> claims);
    Long getTokenId(Jws<Claims> claims);
    String getAppRolesFromRefresh(Jws<Claims> claims);
    String getAppRolesFromAccess(Jws<Claims> claims);
}
