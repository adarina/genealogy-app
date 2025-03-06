package com.ada.genealogyapp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${secret.key}")
    private String jwtSecretKey;

    private final static String ROLES_CLAIM_NAME = "roles";

    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {

        final Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);

        return JWT.create()
                .withSubject(username)
                .withIssuer("Genealogy App")
                .withClaim(ROLES_CLAIM_NAME, roles.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }
}
