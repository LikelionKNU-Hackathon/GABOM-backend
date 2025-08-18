package com.springboot.gabombackend.auth;

import com.springboot.gabombackend.user.User;
import com.springboot.gabombackend.user.UserService;
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

        // JWT 검증 제외 URL
        if (isExcludedPath(requestURI, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 기본은 전체를 토큰으로 사용
        String token = authorizationHeader.trim();

        // Bearer 접두사 제거
        if (authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7).trim();
        }

        // 토큰 만료 여부 확인
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 loginId 추출
        String loginId = JwtTokenUtil.getLoginId(token, secretKey);

        // 사용자 조회
        User loginUser = userService.getByLoginId(loginId);
        if (loginUser == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 인증 객체 생성 (권한 없음)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getId(), null, List.of());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    // JWT 필터 제외 경로
    private boolean isExcludedPath(String uri, String method) {
        return uri.startsWith("/api/users/login") ||
                (uri.startsWith("/api/users") && method.equalsIgnoreCase("POST")
                        && !uri.startsWith("/api/users/logout")) ||
                uri.startsWith("/api/users/find-id") ||
                uri.startsWith("/api/users/find-password") ||
                uri.startsWith("/api/users/verify-code") ||
                uri.startsWith("/api/users/reset-password");
    }
}
