package com.leopold.modules.security.tokenExtractor;

import com.leopold.modules.security.tokenExtractor.ExtractTokenStrategy;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Enumeration;

@Component
class ExtractTokenHeadersStrategy implements ExtractTokenStrategy {
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
