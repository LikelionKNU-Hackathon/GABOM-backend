package com.springboot.gabombackend.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtTokenUtil {

    // JWT 생성
    public static String createToken(String loginId, String secretKey, long expireTimeMs) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(Map.of("loginId", loginId)) // 클레임에 loginId 저장
                .setSubject(loginId) // 주제
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    // 토큰에서 loginId 추출
    public static String getLoginId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("loginId", String.class);
    }

    // 토큰 만료 여부 확인
    public static boolean isExpired(String token, String secretKey) {
        Date exp = extractClaims(token, secretKey).getExpiration();
        return exp.before(new Date());
    }

    // 클레임 추출
    private static Claims extractClaims(String token, String secretKey) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
