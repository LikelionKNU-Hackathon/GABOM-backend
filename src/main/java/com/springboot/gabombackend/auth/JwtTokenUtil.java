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

    // 일반 유저 토큰 생성 (loginId + role)
    public static String createToken(String loginId, String role, String secretKey, long expireTimeMs) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(Map.of(
                        "loginId", loginId,
                        "role", role
                ))
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 업주 토큰 생성 (loginId + role + storeId)
    public static String createTokenWithRoleAndStore(
            String loginId,
            String role,
            Long storeId,
            String secretKey,
            long expireTimeMs
    ) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(Map.of(
                        "loginId", loginId,
                        "role", role,
                        "storeId", storeId
                ))
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // IoginId 추출
    public static String getLoginId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("loginId", String.class);
    }

    // role 추출
    public static String getRole(String token, String secretKey) {
        return extractClaims(token, secretKey).get("role", String.class);
    }

    // storeId 추출 (OWNER 전용)
    public static Long getStoreId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("storeId", Long.class);
    }

    // 만료 여부 확인
    public static boolean isExpired(String token, String secretKey) {
        Date exp = extractClaims(token, secretKey).getExpiration();
        return exp.before(new Date());
    }

    // 내부 Claims 추출
    private static Claims extractClaims(String token, String secretKey) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
