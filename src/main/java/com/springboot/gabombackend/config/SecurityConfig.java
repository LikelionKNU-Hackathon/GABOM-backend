package com.springboot.gabombackend.config;

import com.springboot.gabombackend.jwt.JwtTokenFilter;
import com.springboot.gabombackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Value("${springboot.jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // HTTP Basic 인증 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                // CSRF 비활성화 (JWT 사용 시 필요 없음)
                .csrf(csrf -> csrf.disable())
                // 세션 사용 안 함 (STATELESS)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // JWT 필터 등록
                .addFilterBefore(new JwtTokenFilter(userService, secretKey),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // POST 요청: 회원가입, 로그인 -> 인증 없이 허용
                        .requestMatchers(HttpMethod.POST,
                                "/api/users",
                                "/api/users/login"
                        ).permitAll()
                        // POST 요청: 로그아웃 -> 인증 필요
                        .requestMatchers(HttpMethod.POST, "/api/users/logout").authenticated()
                        // GET 요청: 중복 확인 -> 인증 없이 허용
                        .requestMatchers(HttpMethod.GET, "/api/users/check").permitAll()
                        // 마이페이지 관련 -> 인증 필요
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers("/api/user/stamps").authenticated()
                        // 칭호
                        .requestMatchers(HttpMethod.GET, "/api/users/me/titles").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/me/titles/**").permitAll()
                        // 티어
                        .requestMatchers("/api/users/me/tiers").authenticated()
                        // 나머지 요청: 현재는 모두 허용 (추후 보호 필요시 변경)
                        .anyRequest().permitAll()
                )
                .build();
    }
}
