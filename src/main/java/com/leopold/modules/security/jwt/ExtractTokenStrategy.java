package com.leopold.modules.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

public interface ExtractTokenStrategy {
    String tokenHeader = "Authorization";
    String extractToken(HttpServletRequest req);
}
