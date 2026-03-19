package com.webbeaga.sistema.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${webbeaga.jwt.secret}")
    private String secret;

    @Value("${webbeaga.jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignKey())
            .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(
            Jwts.parser().verifyWith(getSignKey()).build()
                .parseSignedClaims(token).getPayload());
    }

    private SecretKey getSignKey() {
        byte[] src = secret.getBytes();
        byte[] key = new byte[32];
        System.arraycopy(src, 0, key, 0, Math.min(src.length, 32));
        return Keys.hmacShaKeyFor(key);
    }
}
