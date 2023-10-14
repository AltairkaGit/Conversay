package com.leopold.modules.security.authentication;

import com.leopold.modules.security.composer.FilterSequence;
import com.leopold.modules.security.composer.context.ComposerContext;
import com.leopold.modules.security.composer.context.ComposerContextEnum;
import com.leopold.modules.security.users.UserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

@Component
@Order(3)
public class AuthenticationFilterSequence implements FilterSequence {
    private final UserDetailsService userDetailsService;

    public AuthenticationFilterSequence(
            UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, ComposerContext context)
            throws ServletException, IOException, AuthenticationException, NoSuchElementException {
        UserDetails userDetails = userDetailsService.loadByUserId(context.get(ComposerContextEnum.UserId));
        Collection<? extends  GrantedAuthority> authorities = userDetails.getAuthorities();
        authorities.addAll(context.get(ComposerContextEnum.ChatAuthorities));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", authorities));
    }
}
