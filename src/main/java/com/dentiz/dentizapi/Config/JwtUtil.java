package com.dentiz.dentizapi.Config;

import org.springframework.stereotype.Component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtUtil {
    private static String secretKey="D4nt1z_4p1";
    private static Algorithm algorithm;

    static {
        try {
            algorithm = Algorithm.HMAC256(secretKey);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("Platzi Pizza")
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public boolean verifyToken(String token) {
        try {
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return JWT.require(algorithm).build().verify(token).getSubject();
    }

}
