package com.leopold.modules.security.tokenExtractor;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenExtractor {
    String extract(HttpServletRequest req);
}
