package com.springboot.gabombackend.config;

import com.springboot.gabombackend.auth.JwtTokenFilter;
import com.springboot.gabombackend.owner.repository.OwnerRepository;
import com.springboot.gabombackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final OwnerRepository ownerRepository;

    @Value("${springboot.jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenFilter(userService, ownerRepository, secretKey),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 유저 회원가입/로그인
                        .requestMatchers(HttpMethod.POST, "/api/users", "/api/users/login").permitAll()

                        // 업주 회원가입/로그인
                        .requestMatchers(HttpMethod.POST, "/api/owners/signup", "/api/owners/login").permitAll()

                        // 로그아웃 (유저/업주 모두 인증 필요)
                        .requestMatchers(HttpMethod.POST, "/api/users/logout").authenticated()

                        // 유저 API
                        .requestMatchers(HttpMethod.GET, "/api/users/check").permitAll()
                        .requestMatchers("/api/users/me").authenticated()
                        .requestMatchers("/api/user/stamps").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/me").authenticated()

                        // 칭호
                        .requestMatchers(HttpMethod.GET, "/api/users/me/titles").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/me/titles/**").authenticated()

                        // 티어
                        .requestMatchers("/api/users/me/tiers").authenticated()

                        // 케이스
                        .requestMatchers(HttpMethod.GET, "/api/journal/cases").authenticated()

                        // 랭킹
                        .requestMatchers(HttpMethod.GET, "/api/rankings").authenticated()

                        // QR, 스탬프 적립
                        .requestMatchers(HttpMethod.POST, "/api/visits/verify").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/stamps").authenticated()

                        // 리뷰 API
                        .requestMatchers(HttpMethod.GET, "/api/stores/*/reviews").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/stores/*/reviews").authenticated()

                        // 가게 상세/검색 → 공개 허용
                        .requestMatchers(HttpMethod.GET, "/api/stores/**").permitAll()

                        // 챗봇
                        .requestMatchers(HttpMethod.POST, "/api/chat").authenticated()

                        // 업주 전용 API (가입/로그인 제외)
                        .requestMatchers("/api/owners/**").hasRole("OWNER")

                        // 상점 페이지 진입(/api/store), 교환(/api/stamps/exchange), 내 교환 내역(/api/users/me/rewards)은 로그인 필수
                        .requestMatchers(HttpMethod.GET, "/api/store").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/stamps/exchange").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/me/rewards").authenticated()

                        // 나머지 요청 전부 허용
                        .anyRequest().permitAll()
                )
                .build();
    }
}
