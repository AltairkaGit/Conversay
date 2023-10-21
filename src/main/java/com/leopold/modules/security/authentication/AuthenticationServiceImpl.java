package com.leopold.modules.security.authentication;

import com.leopold.modules.security.composer.context.ComposerContextEnum;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.security.users.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationServiceImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication getAuthentication(Long userId, List<GrantedAuthority> authorities) {
        UserDetails userDetails = userDetailsService.loadByUserId(userId);
        Collection<? extends GrantedAuthority> a = userDetails.getAuthorities();
        authorities.addAll(a);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}
