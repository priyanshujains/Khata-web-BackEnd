package com.example.KhataWeb.Security;


import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String secret = "bXlzZWNyZXRrZXlpc3RoZmtkamZpZGZpc2prc3N1c2lzanNkbmpzamNvc2tjbA==";

    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Set<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> rolesList = claims.get("roles", List.class);
        return new HashSet<>(rolesList);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new java.util.Date());
    }
}
