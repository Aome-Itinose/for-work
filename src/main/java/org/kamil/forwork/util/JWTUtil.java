package org.kamil.forwork.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {

    @Value("${secretWord}")
    private String secretWord;
    @Value("${issuer}")
    private String issuer;
    @Value("${expirationMinutes}")
    private Integer expirationMinutes;
    private final String subject = "User details";

    public String generateToken(String username){
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(expirationMinutes).toInstant());
        log.debug("Token is generated");
        return JWT.create()
                .withSubject(subject)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secretWord));
    }

    public String validateTokenAndRetrievedClaim(String token){
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretWord))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        log.debug("Token is validated");
        return jwt.getClaim("username").asString();
    }
}
