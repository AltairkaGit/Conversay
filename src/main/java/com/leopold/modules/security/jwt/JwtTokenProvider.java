package com.leopold.modules.security.jwt;

import com.leopold.modules.appRole.entity.AppRoleEntity;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface JwtTokenProvider {
    String createToken(Long userId, String username, List<AppRoleEntity> roles);
    Authentication getAuthentication(String token);
    String getUsername(String token);
    Long getUserId(String toket);
    boolean validateToken(String token);
    String resolveToken(HttpServletRequest req);
}
