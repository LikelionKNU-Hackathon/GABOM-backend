package com.springboot.gabombackend.config;

import com.springboot.gabombackend.auth.JwtTokenFilter;
import com.springboot.gabombackend.user.service.UserService;
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
                        // Preflight(OPTIONS) 요청 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ 유저 회원가입/로그인 허용
                        .requestMatchers(HttpMethod.POST,
                                "/api/users",
                                "/api/users/login"
                        ).permitAll()

                        // ✅ 업주 회원가입/로그인 허용
                        .requestMatchers(HttpMethod.POST,
                                "/api/owners/signup",
                                "/api/owners/login"
                        ).permitAll()

                        // 로그아웃 -> 인증 필요
                        .requestMatchers(HttpMethod.POST, "/api/users/logout").authenticated()

                        // 유저 API
                        .requestMatchers(HttpMethod.GET, "/api/users/check").permitAll()
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers("/api/user/stamps").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/me").authenticated()

                        // 칭호
                        .requestMatchers(HttpMethod.GET, "/api/users/me/titles").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/me/titles/**").permitAll()

                        // 티어
                        .requestMatchers("/api/users/me/tiers").authenticated()

                        // 케이스
                        .requestMatchers(HttpMethod.GET, "/api/journal/cases").authenticated()

                        // 랭킹
                        .requestMatchers(HttpMethod.GET, "/api/rankings").authenticated()

                        // QR, 스탬프 적립
                        .requestMatchers(HttpMethod.POST, "/api/visits/verify").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/stamps").authenticated()

                        // 가게 검색
                        .requestMatchers("/api/stores/**").authenticated()

                        // 챗봇
                        .requestMatchers(HttpMethod.POST, "/api/chat").authenticated()

                        // ✅ 업주 전용 API (가입/로그인 제외)
                        .requestMatchers("/api/owners/**").hasAuthority("ROLE_OWNER")

                        // 나머지 요청: 현재는 모두 허용
                        .anyRequest().permitAll()
                )
                .build();
    }
}