package com.leopold.config;

import com.leopold.modules.security.chatAuthorization.ChatAuthorizationConfigurer;
import com.leopold.modules.security.jwt.ExtractTokenStrategy;
import com.leopold.modules.security.jwt.JwtTokenConfigurer;
import com.leopold.modules.security.jwt.JwtTokenFilter;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.security.jwt.impl.ExtractTokenCookieStrategy;
import com.leopold.modules.security.jwt.impl.ExtractTokenHeadersStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Order(2)
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatService chatService;
    private final UserDetailsService userDetailsService;
    private final String storage = "/storage/**";
    private final String[] login = {
            "/api/v**/login",
            "/api/v**/auth",
            "/api/v**/refresh"
    };
    private final String[] swagger = {
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**"
    };

    private final String[] ws = {
            "/ws/**",
            "/app/**"
    };
    private final String[] chat = {
      "/api/v**/chat/**"
    };
    private final String[] wsChat = {
            "/ws/chat/**",
            "/app/chat/**"
    };
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, ChatService chatService, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatService = chatService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Order(1)
    @SuppressWarnings("all")
    public SecurityFilterChain apiV1(HttpSecurity http) throws Exception {
        return apiHttpSecurity(http, "/api/v1/**", new ExtractTokenCookieStrategy()).build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiV2(HttpSecurity http) throws Exception {
        return apiHttpSecurity(http, "/api/v2/**", new ExtractTokenHeadersStrategy()).build();
    }

    @SuppressWarnings("all")
    private HttpSecurity apiHttpSecurity(HttpSecurity http, String securityMatcher, ExtractTokenStrategy strategy) throws Exception {
        http
                .securityMatcher(securityMatcher)
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(storage).permitAll()
                        .requestMatchers(swagger).permitAll()
                        .requestMatchers(login).permitAll()
                        .requestMatchers(wsChat).authenticated()
                        .requestMatchers(ws).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(new JwtTokenConfigurer(new JwtTokenFilter(jwtTokenProvider, strategy, userDetailsService)))
                .and()
                .apply(new ChatAuthorizationConfigurer(chatService));
        return http;
    }

    @Bean
    @Order(3)
    @SuppressWarnings("all")
    public SecurityFilterChain base(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(storage).permitAll()
                        .requestMatchers(swagger).permitAll()
                        .requestMatchers(login).permitAll()
                        .requestMatchers(wsChat).authenticated()
                        .requestMatchers(ws).permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE", "UPDATE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("X-Requested-With", "X-HTTP-Method-Override", "Content-Type", "Accept", "Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
