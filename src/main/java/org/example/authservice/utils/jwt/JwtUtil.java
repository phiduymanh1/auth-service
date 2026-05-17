package org.example.authservice.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtUtil {

  @Value("${app.jwt.secret}")
  private String secret;

  @Value("${app.jwt.access-token-expiration}")
  private Long expirationAccessToken;

  @Value("${app.jwt.refresh-token-expiration}")
  private Long expirationRefreshToken;

  /** Get signing key from secret */
  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  /** Generate access token */
  public String generateAccessToken(UserDetails userDetails, UUID applicationId, List<String> permissions) {
    List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

    return Jwts.builder()
            .subject(userDetails.getUsername())
            .id(UUID.randomUUID().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationAccessToken))
            .claim("applicationId", applicationId.toString())
            .claim("roles", roles)
            .claim("permissions", permissions)
            .signWith(getKey())
            .compact();
  }

  /** Generate refresh token */
  public String generateRefreshToken(String username, UUID applicationId) {
    return Jwts.builder()
            .subject(username)
            .id(UUID.randomUUID().toString())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationRefreshToken))
            .claim("applicationId", applicationId.toString())
            .signWith(getKey())
            .compact();
  }

  /** Build JWT token */
  public String buildToken(String username, Long exp) {
    return Jwts.builder()
        .setSubject(username)
        .setId(UUID.randomUUID().toString()) // TODO: add blacklist token
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + exp))
        .signWith(getKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /** Extract username from token */
  public String extractUserName(String token) {
    return parseClaims(token).map(Claims::getSubject).orElse(null);
  }

  /** Extract application ID from token */
  public String extractApplicationId(String token) {
    return parseClaims(token).map(claims -> claims.get("applicationId", String.class)).orElse(null);
  }

  /** Validate access token */
  public boolean isAccessTokenValid(String token, UserDetails userDetails) {
    return parseClaims(token)
        .map(
            c ->
                c.getSubject().equals(userDetails.getUsername())
                    && c.getExpiration().after(new Date()))
        .orElse(false);
  }

  /** Validate refresh token */
  public boolean isRefreshTokenValid(String token) {
    return parseClaims(token).map(c -> c.getExpiration().after(new Date())).orElse(false);
  }

  /** Parse claims from token */
  private Optional<Claims> parseClaims(String token) {
    try {
      return Optional.of(

              Jwts.parser()
                      .verifyWith(getKey())
                      .build()
                      .parseSignedClaims(token)
                      .getPayload()

      );
    } catch (JwtException e) {
      return Optional.empty();
    }
  }

  /** Get JTI from token */
  public String getJti(String token) {
    return parseClaims(token).map(Claims::getId).orElse(null);
  }

  /** Get remaining time from token */
  public long getRemainingTime(String token) {
    return parseClaims(token)
        .map(Claims::getExpiration)
        .map(exp -> exp.getTime() - System.currentTimeMillis())
        .orElse(0L);
  }
}
