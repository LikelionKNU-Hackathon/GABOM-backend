package com.springboot.gabombackend.jwt;

import com.springboot.gabombackend.entity.User;
import com.springboot.gabombackend.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // JWT 검증 제외할 URL
        if (requestURI.startsWith("/api/users/login") ||
                (requestURI.startsWith("/api/users") && request.getMethod().equals("POST")) ||
                requestURI.startsWith("/api/users/find-id") ||
                requestURI.startsWith("/api/users/find-password") ||
                requestURI.startsWith("/api/users/verify-code") ||
                requestURI.startsWith("/api/users/reset-password")) {

            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Authorization 헤더가 없으면 통과
        if (authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 형식이 아니면 통과
        if (!authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer" 뒤의 토큰만 추출
        String token = authorizationHeader.split(" ")[1];

        // 토큰 만료면 통과
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 loginId 추출
        String loginId = JwtTokenUtil.getLoginId(token, secretKey);

        // loginId로 사용자 조회
        User loginUser = userService.getByLoginId(loginId);
        if (loginUser == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증 객체 생성 (권한 없음: 빈 리스트)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getLoginId(), null, List.of());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
