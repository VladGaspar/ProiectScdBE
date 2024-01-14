package com.utcn.demo.security;

import com.utcn.demo.employee.Employee;
import com.utcn.demo.employee.EmployeeRepository;
import com.utcn.demo.employee.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";
    private final Duration validityTime;
    private final SecretKey key;
    private final EmployeeRepository employeeRepository;

    public JwtTokenProvider(@Value("${auth.jwt.validityTime.days}") long validityTime,
                            @Value("${auth.jwt.key}") String secretKey,
                            EmployeeRepository employeeRepository) {
        this.validityTime = Duration.ofDays(validityTime);
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.employeeRepository = employeeRepository;
    }

    public String createAuthToken(final String username, Role role) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime validity = LocalDateTime.now().plus(validityTime);
        final Claims claims = Jwts.claims().setSubject(username);

        claims.put("auth",Collections.singleton(new SimpleGrantedAuthority(role.getAuthority())));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(java.sql.Timestamp.valueOf(now))
                .setExpiration(java.sql.Timestamp.valueOf(validity))
                .signWith(key)
                .compact();
    }

    public String getUsername(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims()
                    .getSubject();
        }
    }

    public String resolveAuthToken(final HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (null != bearerToken && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public boolean validateToken(final String token) {
        if (token == null) {
            return false;
        }
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT Token has expired: " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is not supported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: " + e.getMessage());
        }
   return false;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        final String username = getUsername(token);
        final Employee employee = employeeRepository.findByUsername(username).orElseThrow();
        final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(employee.getRole().getAuthority());

        return new UsernamePasswordAuthenticationToken(employee, "", Collections.singleton(authority));
}
}