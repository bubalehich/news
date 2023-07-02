package ru.clevertec.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.clevertec.auth.model.TokenDto;
import ru.clevertec.auth.model.TokenValidationResponse;
import ru.clevertec.auth.service.UserService;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    private static final String ROLES = "roles";

    private static final String TYPE = "Bearer";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private Long expired;

    private UserService userService;

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public TokenValidationResponse validate(String token) {
        String jwt = token.substring(7);
        String email = extractUsername(jwt);
        String roles = extractClaim(jwt, claims -> claims.get("roles")).toString();
        String role = roles.lines()
                .map(s -> s.substring(s.indexOf("=") + 1, s.length() - 2))
                .findFirst()
                .orElse("");
        var tokenResponse = new TokenValidationResponse();
        tokenResponse.setEmail(email);
        tokenResponse.setRole(role);
        tokenResponse.setValid(true);

        return tokenResponse;
    }


    public TokenDto generateToken(UserDetails userDetails) {
        var issuedAt = new Date(System.currentTimeMillis());
        var expiredAt = new Date(System.currentTimeMillis() + expired);

        var token = Jwts.builder()
                .setClaims(Map.of(ROLES, userDetails.getAuthorities()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedAt)
                .setExpiration(expiredAt)
                .signWith(getSigningKey(), ALGORITHM)
                .compact();

        return new TokenDto(expiredAt, token, TYPE);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
