package com.github.alwaysseen.merchsite.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/auth/**", "/error").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/category", "/api/category/id/**", "/api/item", "/api/item/id/**", "/api/item/category/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/category/create", "/api/item/create").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/category/update/**", "/api/item/update/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/category/delete/**", "/api/item/delete/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/paypal/refund/**").permitAll()
                                .requestMatchers("/api/paypal/checkout", "/api/paypal/checkout/success").permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }
}
