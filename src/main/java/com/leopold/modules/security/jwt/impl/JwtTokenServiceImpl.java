package com.leopold.modules.security.jwt.impl;

import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.security.jwt.JwtTokenService;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenServiceImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean matches(String username, String jwtToken) {
        String tokenUsername = jwtTokenProvider.getUsername(jwtToken);
        return username.equals(tokenUsername);
    }
}
