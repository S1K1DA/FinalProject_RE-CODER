package com.heartlink.member.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    private int jwtExpirationInMs = 86400000; // JWT 유효 기간 (예: 24시간)
    private int refreshTokenExpirationInMs = 604800000; // 7일 (7일 * 24시간 * 60분 * 60초 * 1000밀리초)


    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 토큰에서 사용자 번호 추출
    public int getUserNumberFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("userNumber");
    }

    // 토큰에서 어드민 번호 추출
    public int getAdminNumberFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("adminUserNo");
    }

    // JWT 토큰 생성
    public String generateToken(String email, int userNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("userNumber", userNumber)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // JWT 토큰 생성 (어드민)
    public String generateAdminToken(String email, int adminUserNo) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("adminUserNo", adminUserNo)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(String email, int userNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationInMs); // 리프레시 토큰 유효기간 설정

        return Jwts.builder()
                .setSubject(email)
                .claim("userNumber", userNumber)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    // 토큰 유효성 검사
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            // 유효하지 않은 토큰의 경우 false 반환
            return false;
        }
    }
}
