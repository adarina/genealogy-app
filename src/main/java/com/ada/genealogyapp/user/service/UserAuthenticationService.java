package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.exceptions.InvalidCredentialsException;
import com.ada.genealogyapp.user.dto.UserLoginRequest;
import com.ada.genealogyapp.user.dto.UserLoginResponse;
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

    public UserLoginResponse loginUser(UserLoginRequest userLoginRequest) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = userSearchService.findByUsername(userLoginRequest.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password for username: " + userLoginRequest.getUsername()));

        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            log.error("Invalid username or password for username: {}", userLoginRequest.getUsername());
            throw new InvalidCredentialsException("Invalid username or password for username: " + userLoginRequest.getUsername());
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword()));

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
            return new UserLoginResponse(user.getUsername(), token, user.getId(), user.getRole());
        } catch (Exception exception) {
            log.error("Login failed for user: {}", userLoginRequest.getUsername());
            throw new InvalidCredentialsException("Login failed for user: " + userLoginRequest.getUsername());
        }
    }
}

