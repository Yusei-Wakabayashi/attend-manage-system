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
        .csrf(csrf -> csrf.disable()) // テスト簡略化のためCSRF無効化(本番では必要)
        .authorizeHttpRequests(auth -> auth // 認証情報がなくてもリクエストできるように(本番ではログインとCSRFトークン以外必要)
            .requestMatchers
            (
                new AntPathRequestMatcher("/api/send/login"),
                new AntPathRequestMatcher("/api/send/logout"),
                new AntPathRequestMatcher("/api/send/approverset"),
                new AntPathRequestMatcher("/api/send/style"),
                new AntPathRequestMatcher("/api/send/shift"),
                new AntPathRequestMatcher("/api/reach/approverlist"),
                new AntPathRequestMatcher("/api/reach/allstylelist"),
                new AntPathRequestMatcher("/api/reach/shiftlist"),
                new AntPathRequestMatcher("/api/reach/attendlist"),
                new AntPathRequestMatcher("/api/reach/accountinfo"),
                new AntPathRequestMatcher("/api/reach/requestdetil/shift"),
                new AntPathRequestMatcher("/api/reach/requestdetil/changetime"),
                new AntPathRequestMatcher("/dummy/reach/shiftlist"),
                new AntPathRequestMatcher("/dymmy/reach/attendlist")
            )
            .permitAll()
        )
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable());
        return http.build();
    }
}