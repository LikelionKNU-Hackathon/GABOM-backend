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
                        .allowedOrigins(
                                "https://gabom.shop",
                                "https://www.gabom.shop",
                                "https://gabom.netlify.app",
                                "http://localhost:3000"
                        )
                        .allowedMethods(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS",
                                "HEAD"
                        )
                        .allowedHeaders("*")
                        .exposedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
