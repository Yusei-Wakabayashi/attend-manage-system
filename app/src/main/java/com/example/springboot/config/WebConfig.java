package com.example.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer
{
    // スーパークラスのメソッドを上書きする
    @Override
    // corsに追加
    public void addCorsMappings(CorsRegistry registry)
    {
        registry.addMapping("/**") // すべてのパス
        .allowedOrigins("http://localhost") // "*"(ワイルドカード)ではなく明示する
        .allowCredentials(true) // 認証情報も許可
        .allowedMethods("*"); // すべてのリクエストメソッド
    }
}
