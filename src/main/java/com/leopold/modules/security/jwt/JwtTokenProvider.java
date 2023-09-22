package com.leopold.modules.security.jwt;

import com.leopold.modules.appRole.entity.AppRoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;
import java.util.List;

public interface JwtTokenProvider {
    long EXPIRED_ACCESS_MS = 30 * 24 * 60 * 1000;
    long EXPIRED_REFRESH_MS = 365 * 24 * 60 * 1000;
    String createAccess(String refresh);
    String createRefresh(Long userId, String username, List<AppRoleEntity> roles);
    boolean validateAccess(String access);
    boolean validateRefresh(String refresh);
    Jws<Claims> getClaims(String token);
    String getRefreshFromAccess(String access);
    String getUsernameFromAccess(Jws<Claims> claims);
    String getUsernameFromRefresh(Jws<Claims> claims);
    Date getExpiration(Jws<Claims> claims);
    Long getUserId(Jws<Claims> claims);
    String getAppRolesFromRefresh(Jws<Claims> claims);
    String getAppRolesFromAccess(Jws<Claims> claims);
}
