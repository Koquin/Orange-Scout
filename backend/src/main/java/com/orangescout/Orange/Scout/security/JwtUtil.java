package com.orangescout.Orange.Scout.security;

import com.orangescout.Orange.Scout.model.AppUser;
import com.orangescout.Orange.Scout.repository.AppUserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final String SECRET_KEY_ENV = System.getenv("SECRET_KEY");

    private final AppUserRepository appUserRepository;

    public JwtUtil(AppUserRepository appUserRepository) {
        if (SECRET_KEY_ENV == null || SECRET_KEY_ENV.isEmpty()) {
            throw new IllegalArgumentException("The 'SECRET_KEY' environment variable is not defined or is empty. Please define a secure JWT key.");
        }
        this.appUserRepository = appUserRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(AppUser appUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", appUser.getRole());
        return createToken(claims, appUser.getEmail());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        try {
            if (!username.equals(userDetails.getUsername())) {
                return false;
            }
            if (isTokenExpired(token)) {
                return false;
            }
            return appUserRepository.existsByEmail(username);
        } catch (MalformedJwtException | SignatureException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY_ENV);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}