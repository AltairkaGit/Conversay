package com.leopold.modules.security.jwt;

import com.leopold.modules.security.composer.FilterSequence;
import com.leopold.modules.security.composer.context.ComposerContext;
import com.leopold.modules.security.composer.context.ComposerContextEnum;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import javax.naming.AuthenticationException;
import java.io.IOException;

public class JwtTokenFilterSequenceImpl implements FilterSequence {
    private final JwtTokenProvider jwtTokenProvider;
    private final ExtractTokenStrategy extractTokenStrategy;

    public JwtTokenFilterSequenceImpl(
            JwtTokenProvider jwtTokenProvider,
            ExtractTokenStrategy extractTokenStrategy
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractTokenStrategy = extractTokenStrategy;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, ComposerContext context)
            throws IOException, ServletException, AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String access = extractTokenStrategy.extractToken(httpServletRequest);
        if (access == null ) throw new AuthenticationException("no authorization header");
        if (access.isEmpty()) throw new AuthenticationException("empty access token");
        if (!jwtTokenProvider.validateAccess(access)) throw new AuthenticationException("access token is not ok");
        context.put(ComposerContextEnum.UserId, jwtTokenProvider.getUserId(jwtTokenProvider.getClaims(access)));
        Long userId = jwtTokenProvider.getUserId(jwtTokenProvider.getClaims(access));
        request.setAttribute("reqUserId", userId);
        request.setAttribute("accessToken", access);
    }
}
