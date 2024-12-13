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

   @PostConstruct
    public void init(){
       key = Keys.hmacShaKeyFor(tokenSecretKey.getBytes(StandardCharsets.UTF_8));
   }

   // 토큰 생성
   public String generateAccessToken(User user){
       return Jwts.builder()
           .setSubject(user.getId().toString())
           .claim("email", user.getEmail())
           .claim("roleType", user.getRole().toString())
           .claim("loginType", user.getLoginType())
           .setIssuedAt(new Date((System.currentTimeMillis())))
           .setExpiration(new Date(System.currentTimeMillis() + tokenExpiresIn))
           .signWith(key, SignatureAlgorithm.HS256)
           .compact();
   }

   // Oauth 전용 토큰 생성
    public String generateAccessTokenForOauth(String userId, String email, String loginType, String oauthId){
       return Jwts.builder()
           .setSubject(userId)
           .claim("email", email)
           .claim("loginType", loginType)
           .claim("oauthId", oauthId)
           .setIssuedAt(new Date(System.currentTimeMillis()))
           .setExpiration(new Date(System.currentTimeMillis() + tokenExpiresIn))
           .signWith(key, SignatureAlgorithm.HS256)
           .compact();
    }

   // 토큰 검증
   public void validate(String accessToken){
       try{
           Jwts.parserBuilder()
               .setSigningKey(key)
               .build()
               .parseClaimsJws(accessToken);
               // .getBody()
               // .getSubject();
       }catch(ExpiredJwtException e){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
       }catch (JwtException e){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
       }
   }

   public Long extractUserId(String accessToken){
       return Long.valueOf(getClaims(accessToken).getSubject());
   }

   public String extractEmail(String accessToken){
       return getClaims(accessToken).get("email", String.class);
   }

   public String extractRoleType(String accessToken){
       return getClaims(accessToken).get("roleType", String.class);
   }

   public String extractLoginType(String accessToken){
       return getClaims(accessToken).get("loginType", String.class);
   }

    public Claims getClaims(String token) {
       try{
           return Jwts.parserBuilder()
               .setSigningKey(key)
               .build()
               .parseClaimsJws(token)
               .getBody();
       } catch(ExpiredJwtException e){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
       }catch(JwtException e){
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
       }
    }
}
