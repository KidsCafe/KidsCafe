package com.sparta.kidscafe.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.sparta.kidscafe.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${jwt.token.secret.key}")
    private String tokenSecretKey;

    @Value("${jwt.token.expires.in}")
    private Long tokenExpiresIn;

    private Key key;

    public JwtUtil() {}

    public JwtUtil(String tokenSecretKey, Long tokenExpiresIn) {
        this.tokenSecretKey = tokenSecretKey;
        this.tokenExpiresIn = tokenExpiresIn;
        this.key = Keys.hmacShaKeyFor(tokenSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(tokenSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    // 토큰 생성
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setIssuer("kidscafe")
                .setAudience("user")
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roleType", user.getRole().toString())
                .claim("loginType", user.getLoginType().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiresIn))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 검증
    public void validate(String accessToken) {
        parseToken(accessToken); // 검증만 수행
    }

    public Long extractUserId(String accessToken) {
        return Long.valueOf(getClaims(accessToken).getSubject());
    }

    public String extractEmail(String accessToken) {
        return getClaims(accessToken).get("email", String.class);
    }

    public String extractRoleType(String accessToken) {
        return getClaims(accessToken).get("roleType", String.class);
    }

    private Claims getClaims(String token) {
        return parseToken(token);
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
        }
    }
}