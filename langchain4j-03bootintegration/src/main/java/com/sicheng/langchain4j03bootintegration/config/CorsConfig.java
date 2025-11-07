package com.sicheng.langchain4j03bootintegration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 允许所有源（生产环境建议改成 http://localhost:5173）
        config.addAllowedHeader("*");        // 允许所有请求头
        config.addAllowedMethod("*");        // 允许所有方法：GET/POST/PUT/DELETE...
        config.setAllowCredentials(true);    // 允许携带 Cookie、Authorization 等凭证

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}