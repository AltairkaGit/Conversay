package com.leopold.modules.security.jwt;

import jakarta.transaction.Transactional;

@Transactional
public interface JwtTokenService {
    boolean matches(String username, String jwtToken);
}
