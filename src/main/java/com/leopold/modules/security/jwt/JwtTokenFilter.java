package com.leopold.modules.security.jwt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final ExtractTokenStrategy extractTokenStrategy;
    private final UserDetailsService userDetailsService;


    public JwtTokenFilter(
            JwtTokenProvider jwtTokenProvider,
            ExtractTokenStrategy extractTokenStrategy,
            @Qualifier("jwtUserDetailService") UserDetailsService userDetailsService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.extractTokenStrategy = extractTokenStrategy;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String access = extractTokenStrategy.extractToken(httpServletRequest);
        if (access != null && jwtTokenProvider.validateAccess(access)) {
            Authentication authentication = getAuthentication(jwtTokenProvider.getUsernameFromAccess(jwtTokenProvider.getClaims(access)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Long userId = jwtTokenProvider.getUserId(jwtTokenProvider.getClaims(access));
            request.setAttribute("reqUserId", userId);
            request.setAttribute("accessToken", access);
        }
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
