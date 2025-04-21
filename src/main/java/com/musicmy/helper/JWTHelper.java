package com.musicmy.helper;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTHelper {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.subject}")
    private String SUBJECT;

    @Value("${jwt.subject}")
    private String SUBJECT_VALIDATION;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public SecretKey getSecretKey() {
        String secretKey = SECRET_KEY;
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Map<String, String> claims) {
        return io.jsonwebtoken.Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(claims)
                .subject(SUBJECT)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60000000))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }


    public String generateValidateToken(Map<String, String> claims) {
        return io.jsonwebtoken.Jwts.builder()
        .id(UUID.randomUUID().toString())
        .claims(claims)
        .subject(SUBJECT_VALIDATION)
        .issuer(ISSUER)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 60000000))
        .signWith(getSecretKey(), Jwts.SIG.HS256)
        .compact();
    }



    private Claims getAllClaimsFromToken(String sToken) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(sToken).getPayload();
    }

    public String validateToken(String sToken) {

        Claims oClaims = getAllClaimsFromToken(sToken);

        if (oClaims.getExpiration().before(new Date())) {
            return null;
        }

        if (oClaims.getIssuedAt().after(new Date())) {
            return null;
        }

        if (!oClaims.getIssuer().equals(ISSUER)) {
            return null;
        }

        if (!oClaims.getSubject().equals(SUBJECT)) {
            return null;
        }

        return oClaims.get("email", String.class);
    }

}