package com.kamalteja.brevify.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamalteja.brevify.security.filter.JwtAuthenticationFilter;
import com.kamalteja.brevify.user.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService userService,
                          PasswordEncoder passwordEncoder,
                          ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configure(http))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register").permitAll()
                        .requestMatchers("/shorten", "/code/**", "/**").authenticated()
                )
//                .exceptionHandling(exceptions -> exceptions
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//                            Map<String, String> errorData = new HashMap<>();
//                            errorData.put(ERROR_CODE, "AUTH-01");
//                            errorData.put(ERROR_MESSAGE, "Authentication failed: " + authException.getMessage());
//
//                            objectMapper.writeValue(response.getOutputStream(),
//                                    ApiResponse.handler(FAILURE, errorData));
//                        })
//                        .accessDeniedHandler((request, response, accessDeniedException) -> {
//                            response.setStatus(HttpStatus.FORBIDDEN.value());
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//                            Map<String, String> errorData = new HashMap<>();
//                            errorData.put(ERROR_CODE, "AUTH-02");
//                            errorData.put(ERROR_MESSAGE, "Access denied: " + accessDeniedException.getMessage());
//
//                            objectMapper.writeValue(response.getOutputStream(),
//                                    ApiResponse.handler(FAILURE, errorData));
//                        })
//                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(List.of(authProvider));
    }
}
