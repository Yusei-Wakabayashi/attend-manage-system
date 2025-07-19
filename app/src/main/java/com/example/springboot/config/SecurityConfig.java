package com.example.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
    http
        .csrf(csrf -> csrf.disable()) // テスト簡略化のためCSRF無効化（本番では必要）
        .authorizeHttpRequests(auth -> auth
            .requestMatchers
            (
                new AntPathRequestMatcher("/api/reach/attendlist"),
                new AntPathRequestMatcher("/api/reach/shiftlist"),
                new AntPathRequestMatcher("/api/send/login"),
                new AntPathRequestMatcher("/api/send/logout")
            )
            .permitAll()
        )
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable());
        return http.build();
    }
}