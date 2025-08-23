package com.springboot.gabombackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(
                                "https://gabom.shop",
                                "https://www.gabom.shop",
                                "https://gabom.netlify.app",
                                "http://localhost:3000"
                        )
                        .allowedMethods("*")       // 모든 메서드 허용
                        .allowedHeaders("*")       // 모든 헤더 허용
                        .exposedHeaders("*")       // 응답 헤더도 다 허용
                        .allowCredentials(true);   // 쿠키/인증 허용
            }
        };
    }
}
