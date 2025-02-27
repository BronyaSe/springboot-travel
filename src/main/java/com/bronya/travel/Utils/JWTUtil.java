package com.bronya.travel.Utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {

    private final Integer Expiration;
    private final Key Secret;

    public JWTUtil(@Value("${jwt.expiration}")Integer expiration,@Value("${jwt.secret}") String secret) throws NoSuchAlgorithmException {
        this.Expiration = expiration;
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha256.digest(secret.getBytes(StandardCharsets.UTF_8));
        this.Secret = Keys.hmacShaKeyFor(keyBytes);
    }


    // 生成 Token
    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Expiration))
                .signWith(Secret)
                .compact();
    }

    // 验证 Token
    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody(); // 返回所有的 Claims
        } catch (JwtException e) {
            throw new RuntimeException("Invalid Token");
        }
    }
}
