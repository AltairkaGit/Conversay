package com.leopold.modules.security.users;

import com.leopold.modules.security.jwt.JwtTokenProvider;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class UsernameTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    public UsernameTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        String username = ((HttpServletRequest) request).getParameter("username");

        String tokenUsername = jwtTokenProvider.getUsername(token);
        if (tokenUsername == null || !tokenUsername.equals(username)) {
            throw new AccessDeniedException("");
        }
        chain.doFilter(request, response);
    }
}
