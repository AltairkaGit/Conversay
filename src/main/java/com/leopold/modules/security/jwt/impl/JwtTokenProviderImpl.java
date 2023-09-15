package com.leopold.modules.security.jwt.impl;

import com.leopold.modules.appRole.entity.AppRoleEntity;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Value("${jwt.token.prefix}")
    private String tokenHeader;
    @Value("${jwt.token.secret}")
    private String secret;
    private SecretKey key;
    @Value("${jwt.token.expired}")
    private long expiredMs;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProviderImpl(@Qualifier("jwtUserDetailService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void postConstruct() {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    @Override
    public String createToken(Long userId, String username, List<AppRoleEntity> roles) {
        Claims claims = Jwts.claims().setSubject(username).setId(userId.toString());
        claims.put("AppRoles", mapAppRolesToStrings(roles));
        Date now = new Date();
        Date expired = new Date(now.getTime() + expiredMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(key)
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public Long getUserId(String token) {
        String id = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getId();
        return Long.parseLong(id);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Date expired = claims.getBody().getExpiration();
            return !expired.before(new Date());
        } catch (JwtException e) {
            //throw new JwtAuthenticationException("JWT token is expired or invalid: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String resolveToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (var cookie : cookies) {
            if (tokenHeader.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private static List<String> mapAppRolesToStrings (List<AppRoleEntity> roles) {
        return roles.stream().map(AppRoleEntity::getRole).collect(Collectors.toList());
    }
}
