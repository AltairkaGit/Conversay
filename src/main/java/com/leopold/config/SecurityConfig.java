package com.leopold.config;

import com.leopold.modules.security.chatAuthorization.ChatAuthorizationConfigurer;
import com.leopold.modules.security.jwt.JwtTokenConfigurer;
import com.leopold.modules.security.jwt.JwtTokenProvider;
import com.leopold.modules.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final String storage = "/storage/**";
    private final String[] login = {
            "/api/v1/login",
            "/api/v1/auth"
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

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, ChatService chatService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatService = chatService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain base(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(storage).permitAll()
                        .requestMatchers(swagger).permitAll()
                        .requestMatchers(login).permitAll()
                        .requestMatchers(ws).permitAll()
                        .anyRequest().authenticated()
                )
                .apply(new JwtTokenConfigurer(jwtTokenProvider))
                .and()
                .apply(new ChatAuthorizationConfigurer(chatService));
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "PUT", "POST", "DELETE", "UPDATE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("X-Requested-With", "X-HTTP-Method-Override", "Content-Type", "Accept", "Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
