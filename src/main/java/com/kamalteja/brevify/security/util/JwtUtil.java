package com.kamalteja.brevify.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.kamalteja.brevify.security.constants.SecurityConstants.USERNAME;

@Component
@Slf4j
public class JwtUtil {

    private final Key key;
    private final long expirationTime;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.expirationTime = expiration * 1000;
    }

    /**
     * Generates a JWT token for the specified username.
     *
     * @param username the username to be included in the token claims
     * @return a signed JWT token containing the username
     */
    public String generateToken(String username) {
        log.info("in JwtUtil -> generateToken");
        return generateToken(Map.of(USERNAME, username));
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((String) claims.get(USERNAME))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT token by checking if it's properly signed, not expired, and contains the expected username.
     *
     * @param token    the JWT token to validate
     * @param username the expected username that should match the subject in the token
     * @return true if the token is valid and contains the expected username, false otherwise
     */
    public boolean isTokenValid(String token, String username) {
        log.info("in JwtUtil -> isTokenValid");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenUsername = claims.getSubject();

            if (tokenUsername == null || !tokenUsername.equals(username)) {
                log.error("Token does not match the expected username");
                return false;
            }

            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token has expired");
            return false;
        } catch (JwtException | IllegalArgumentException exception) {
            log.error("Unable to validate token: {}",
                    Optional.of(exception)
                            .map(Throwable::getMessage)
                            .orElse("unknown error"));
            return false;
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token the JWT token from which to extract the username
     * @return the username extracted from the token
     */
    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get(USERNAME, String.class);
        } catch (Exception exception) {
            log.error("Error extracting username from token: {}",
                    Optional.of(exception)
                            .map(Throwable::getMessage)
                            .orElse("unknown error"));
            throw exception;
        }
    }

    /**
     * Retrieves the username of the currently authenticated user from the security context.
     *
     * @return the username of the authenticated user, or null if no user is authenticated.
     * Returns UserDetails.getUsername() if principal is a UserDetails instance,
     * otherwise returns principal.toString()
     */
    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else {
                return principal.toString();
            }
        }
        return null;
    }
}
