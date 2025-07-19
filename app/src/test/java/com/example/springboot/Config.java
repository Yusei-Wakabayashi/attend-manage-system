package com.example.springboot;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class Config extends WebSecurityConfigurerAdapter
{
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers("/api/send/login","/api/send/logout").permitAll();
    }
}