package com.leopold.config;

import com.leopold.modules.security.authentication.AuthenticationFilterSequenceImpl;
import com.leopold.modules.security.chatAuthorization.ChatAuthorizationFilterSequenceImpl;
import com.leopold.modules.security.composer.FilterComposer;
import com.leopold.modules.security.jwt.ExtractTokenStrategy;
import com.leopold.modules.security.configurer.FiltersConfigurer;
import com.leopold.modules.security.jwt.JwtTokenFilterSequenceImpl;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.chat.service.ChatService;
import com.leopold.modules.security.jwt.impl.ExtractTokenCookieStrategy;
import com.leopold.modules.security.jwt.impl.ExtractTokenHeadersStrategy;
import com.leopold.modules.security.users.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
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
    private final static String[] permitted = {
            "/storage/**",

            "/api/v**/login",
            "/api/v**/auth",
            "/api/v**/refresh",

            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**",

            "/ws/**",
            "/app/**"
    };
    private final static List<String> permittedList = Arrays.asList(permitted);
    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, ChatService chatService, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatService = chatService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiV1(HttpSecurity http) throws Exception {
        return apiHttpSecurity(http, "/api/v1/**", new ExtractTokenCookieStrategy()).build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiV2(HttpSecurity http) throws Exception {
        return apiHttpSecurity(http, "/api/v2/**", new ExtractTokenHeadersStrategy()).build();
    }

    private HttpSecurity apiHttpSecurity(HttpSecurity http, String securityMatcher, ExtractTokenStrategy strategy) throws Exception {
        http
                .securityMatcher(securityMatcher)
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(permitted).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(new FiltersConfigurer(FilterComposer.composeFilters(
                        List.of(
                                new JwtTokenFilterSequenceImpl(jwtTokenProvider, strategy),
                                new ChatAuthorizationFilterSequenceImpl(chatService),
                                new AuthenticationFilterSequenceImpl(userDetailsService)
                        ),
                        permittedList
                )));
        return http;
    }

    @Bean
    @Order(3)
    public SecurityFilterChain base(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(m -> m.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(permitted).permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
