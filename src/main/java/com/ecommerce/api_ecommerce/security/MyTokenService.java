package com.ecommerce.api_ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ecommerce.api_ecommerce.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class MyTokenService {
    @Value("${secret.key}")
    private String secretKey;


    public String createToken(User auth) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("ecommerce-matheus")
                .withExpiresAt(getExpiretionInstant())
                .withSubject(auth.getUsername())
                .withClaim("role", auth.getRole().getRole())
                .sign(algorithm);
    }
    public UserDataFromToken verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var decoderJwt = JWT.require(algorithm)
                .withIssuer("ecommerce-matheus")
                .build();
        String username = decoderJwt.verify(token).getSubject();
        String role = decoderJwt.verify(token).getClaim("role").asString();
        UserDataFromToken userDataFromToken = new UserDataFromToken(username, role);

        return userDataFromToken;
    }

    private Instant getExpiretionInstant() {
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
