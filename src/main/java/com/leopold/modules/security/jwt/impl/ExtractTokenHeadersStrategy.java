package com.leopold.modules.security.jwt.impl;

import com.leopold.modules.security.jwt.ExtractTokenStrategy;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;

public class ExtractTokenHeadersStrategy implements ExtractTokenStrategy {
    @Override
    public String extractToken(HttpServletRequest req) {
        Enumeration<String> headers = req.getHeaderNames();
        if (headers == null) return null;
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            if (tokenHeader.equalsIgnoreCase(header)) {
                return req.getHeader(header);
            }
        }
        return null;
    }
}
