package com.fiap.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;

@Service
public class ClientJwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key getKey() {
        return Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(secret)
        );
    }

    public String getDocumentFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("documentNumber", String.class);
    }
}
