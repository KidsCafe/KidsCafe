package com.sparta.kidscafe.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key secretKey; // JWT 서명에 사용할 비밀 키
    private final long expiration; // JWT 만료 시간 (밀리초 단위)

    /**
     * JwtUtil 생성자
     * 기본 비밀 키와 만료 시간 설정
     */
    public JwtUtil() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 자동으로 비밀 키 생성
        this.expiration = 60 * 60 * 1000; // 1시간 (밀리초 단위)
    }

    /**
     * JWT 토큰 생성
     * @param subject 토큰의 주체 (일반적으로 사용자 ID 또는 이메일)
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(subject) // 토큰의 주체 설정
                .setIssuedAt(now) // 발급 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256) // HMAC SHA256 알고리즘으로 서명
                .compact();
    }

    /**
     * JWT 토큰에서 클레임 정보 추출
     * @param token JWT 토큰
     * @return 토큰에서 추출한 Claims 객체
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // 서명에 사용된 키 설정
                .build()
                .parseClaimsJws(token) // 토큰 파싱 및 유효성 검사
                .getBody(); // Claims 반환
    }

    /**
     * JWT 토큰에서 주체(Subject) 추출
     * @param token JWT 토큰
     * @return 토큰에서 추출한 주체(Subject)
     */
    public String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * JWT 토큰 유효성 검사
     * @param token JWT 토큰
     * @return 유효한 토큰이면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token); // 토큰 파싱 및 유효성 검사
            return true;
        } catch (Exception e) {
            return false; // 유효하지 않은 토큰
        }
    }
}
