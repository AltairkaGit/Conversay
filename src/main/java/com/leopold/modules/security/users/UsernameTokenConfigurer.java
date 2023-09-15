package com.leopold.modules.security.users;

import com.leopold.modules.security.jwt.JwtTokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.preauth.RequestAttributeAuthenticationFilter;

public class UsernameTokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    public UsernameTokenConfigurer (JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        UsernameTokenFilter usernameTokenFilter = new UsernameTokenFilter(jwtTokenProvider);
        http.addFilterBefore(usernameTokenFilter, RequestAttributeAuthenticationFilter.class);
    }

}
