package com.example.springboot.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
    http
        .cors
        (cors -> cors
            .configurationSource
            (
                request -> 
                    {
                    var config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("GET","POST"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(List.of("*"));
                    return config;
                }
            )
        )
        .csrf(csrf -> csrf.disable()) // テスト簡略化のためCSRF無効化(本番では必要)
        .authorizeHttpRequests(auth -> auth // 認証情報がなくてもリクエストできるように(本番ではログインとCSRFトークン以外必要)
            .requestMatchers
            (
                new AntPathRequestMatcher("/api/send/login"),
                new AntPathRequestMatcher("/api/send/logout"),
                new AntPathRequestMatcher("/api/send/approverset"),
                new AntPathRequestMatcher("/api/send/style"),
                new AntPathRequestMatcher("/api/send/shift"),
                new AntPathRequestMatcher("/api/send/changetime"),
                new AntPathRequestMatcher("/api/send/stamp"),
                new AntPathRequestMatcher("/api/send/vacation"),
                new AntPathRequestMatcher("/api/reach/approverlist"),
                new AntPathRequestMatcher("/api/reach/allstylelist"),
                new AntPathRequestMatcher("/api/reach/shiftlist"),
                new AntPathRequestMatcher("/api/reach/attendlist"),
                new AntPathRequestMatcher("/api/reach/accountinfo"),
                new AntPathRequestMatcher("/api/reach/requestdetil/shift"),
                new AntPathRequestMatcher("/api/reach/requestdetil/changetime"),
                new AntPathRequestMatcher("/api/reach/requestdetil/stamp"),
                new AntPathRequestMatcher("/api/reach/requestdetil/vacation"),
                new AntPathRequestMatcher("/api/reach/requestdetil/overtime"),
                new AntPathRequestMatcher("/api/reach/requestdetil/othertime"),
                new AntPathRequestMatcher("/api/reach/requestdetil/monthly"),
                new AntPathRequestMatcher("/api/reach/requestlist"),
                new AntPathRequestMatcher("/dummy/reach/shiftlist"),
                new AntPathRequestMatcher("/dummy/reach/attendlist")
            )
            .permitAll()
        )
        .formLogin(login -> login.disable())
        .httpBasic(basic -> basic.disable());
        return http.build();
    }
}