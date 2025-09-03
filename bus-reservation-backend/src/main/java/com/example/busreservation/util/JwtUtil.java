package com.example.busreservation.util;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final Key key;
    private final long expiryMillis;

    public JwtUtil(org.springframework.core.env.Environment env) {
        String secret = env.getProperty("app.jwt.secret", "devSecretChangeMe");
        int minutes = Integer.parseInt(env.getProperty("app.jwt.expiryMinutes", "120"));
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiryMillis = minutes * 60L * 1000L;
    }

    public String generateToken(String subject, Map<String,Object> claims){
        Instant now = Instant.now();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expiryMillis)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> validate(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
