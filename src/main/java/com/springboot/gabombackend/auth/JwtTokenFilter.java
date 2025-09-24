package com.springboot.gabombackend.auth;

import com.springboot.gabombackend.user.entity.User;
import com.springboot.gabombackend.user.service.UserService;
import com.springboot.gabombackend.owner.entity.Owner;
import com.springboot.gabombackend.owner.repository.OwnerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final OwnerRepository ownerRepository;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // JWT 예외 경로
        if (isExcludedPath(requestURI, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 토큰 추출
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7).trim()
                : authorizationHeader.trim();

        // 만료 확인
        if (JwtTokenUtil.isExpired(token, secretKey)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 정보 추출
        String loginId = JwtTokenUtil.getLoginId(token, secretKey);
        String role = JwtTokenUtil.getRole(token, secretKey);

        UsernamePasswordAuthenticationToken authenticationToken = null;

        if ("OWNER".equals(role)) {
            // 업주 인증 처리
            Owner owner = ownerRepository.findByLoginId(loginId)
                    .orElse(null);
            if (owner != null) {
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        owner.getLoginId(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_OWNER"))
                );
            }
        } else {
            // 일반 사용자 인증 처리
            User loginUser = userService.getByLoginId(loginId);
            if (loginUser != null) {
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        loginUser.getLoginId(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        }

        if (authenticationToken != null) {
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String uri, String method) {
        return uri.startsWith("/api/users/login") ||
                (uri.startsWith("/api/users") && method.equalsIgnoreCase("POST")
                        && !uri.startsWith("/api/users/logout")) ||
                uri.startsWith("/api/users/find-id") ||
                uri.startsWith("/api/users/find-password") ||
                uri.startsWith("/api/users/verify-code") ||
                uri.startsWith("/api/users/reset-password") ||

                // 업주 회원가입/로그인 예외 추가
                uri.startsWith("/api/owners/signup") ||
                uri.startsWith("/api/owners/login");
    }
}
