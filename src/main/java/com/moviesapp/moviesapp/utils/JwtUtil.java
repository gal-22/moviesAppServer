package com.moviesapp.moviesapp.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    public String generateJwtToken(String username) {
        System.out.println("Generating JWT token for username: " + username);
        System.out.println("Token expiration set to: " + jwtExpirationMs + " ms");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
        System.out.println("Token will expire at: " + expiryDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        System.out.println("Generated token (first 20 chars): " + token.substring(0, 20) + "...");
        return token;
    }

    public String getUserNameFromJwtToken(String token) {
        System.out.println("Extracting username from token");
        try {
            String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
            System.out.println("Extracted username: " + username);
            return username;
        } catch (Exception e) {
            System.out.println("Error extracting username: " + e.getMessage());
            throw e;
        }
    }

    public boolean validateJwtToken(String authToken) {
        System.out.println("Validating JWT token");
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            System.out.println("Token validation successful");
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}