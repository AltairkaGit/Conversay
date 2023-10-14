package com.leopold.modules.security.tokenExtractor;

import jakarta.servlet.http.HttpServletRequest;

interface ExtractTokenStrategy {
    String tokenHeader = "Authorization";

    /**
     *
     * @param req
     * @return token or null
     */
    String extractToken(HttpServletRequest req);
}
