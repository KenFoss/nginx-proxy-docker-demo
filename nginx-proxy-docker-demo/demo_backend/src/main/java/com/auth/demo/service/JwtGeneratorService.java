package com.auth.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtGeneratorService {

    private Integer JWT_EXPIRY = (1000*60*60);
    private String JWT_SECRET = "ieBJfAscbYrOSA+Wp7F5QLAIN8uCM5U/wMXM/2nCsbU";

    public String generateToken(String googleId) {
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime()+ JWT_EXPIRY);


        String token = Jwts.builder()
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .claim("googleId", googleId)
                .compact();
        return token;
    }

    private SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getGoogleIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("googleId", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET)
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

}
