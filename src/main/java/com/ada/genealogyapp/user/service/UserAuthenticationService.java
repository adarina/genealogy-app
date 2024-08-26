package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.exceptions.InvalidCredentialsException;
import com.ada.genealogyapp.user.dto.LoginRequest;
import com.ada.genealogyapp.user.dto.LoginResponse;
import com.ada.genealogyapp.user.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Slf4j
@Service
public class UserAuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final UserSearchService userSearchService;


    @Value("${secret.key}")
    private String jwtSecretKey;

    public UserAuthenticationService(AuthenticationManager authenticationManager, UserSearchService userSearchService) {
        this.authenticationManager = authenticationManager;
        this.userSearchService = userSearchService;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = userSearchService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password for username: " + loginRequest.getUsername()));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.error("Invalid username or password for username: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException("Invalid username or password for username: " + loginRequest.getUsername());
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            user = (User) authentication.getPrincipal();

            log.info("Authentication successful for user: {}", user.getUsername());

            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
            String token = JWT.create()
                    .withSubject(user.getUsername())
                    .withIssuer("Ada")
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .sign(algorithm);

            log.info("JWT token generated successfully for user: {}", user.getUsername());
            return new LoginResponse(user.getUsername(), token, user.getId(), user.getRole());
        } catch (Exception exception) {
            log.error("Login failed for user: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException("Login failed for user: " + loginRequest.getUsername());
        }
    }
}

