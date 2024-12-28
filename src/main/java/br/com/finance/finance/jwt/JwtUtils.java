package br.com.finance.finance.jwt;

import br.com.finance.finance.constants.Constants;
import br.com.finance.finance.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtils {
    private final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret-key}")
    private String secretKeyConfig;

    @Value("${jwt.expiration-minutes}")
    private String expirationMinutes;

    private SecretKey secretKey;
    private  long expirationMinutesValue;

    @PostConstruct
    public void initialize() {
        if(secretKeyConfig == null || secretKeyConfig.isBlank()) {
            log.info("SecretKey is falsy.");
        }
        this.expirationMinutesValue = Long.parseLong(expirationMinutes);
        this.secretKey = Keys.hmacShaKeyFor(secretKeyConfig.getBytes(StandardCharsets.UTF_8));
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public JwtToken generateToken(UserModel userModel) {
        Date issuedAt = new Date();
        String token = Jwts.builder()
                .header().keyId(Constants.JWT_AUTHORIZATION).and()
                .claim("username", userModel.getUsername())
                .claim("role", userModel.getRole())
                .issuedAt(issuedAt)
                .expiration(Date.from(issuedAt.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .plusMinutes(expirationMinutesValue)
                        .toInstant()))
                .signWith(this.getSecretKey())
                .compact();
        return new JwtToken(token);
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(refactorToken(token)).getPayload();
        } catch (JwtException ex) {
            log.error("Invalid token {}", ex.getMessage() );
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(refactorToken(token));
            return true;
        } catch (JwtException ex) {
            log.error("Invalid token {}", ex.getMessage() );
        }
        return false;
    }

    private String refactorToken(String token) {
        if(token.startsWith(Constants.JWT_BEARER)) {
            return token.substring(Constants.JWT_BEARER.length());
        }
        return token;
    }
}
