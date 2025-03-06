package com.ada.genealogyapp.auth;

import com.ada.genealogyapp.exceptions.InvalidCredentialsException;
import com.ada.genealogyapp.jwt.JwtUtil;
import com.ada.genealogyapp.passwords.PasswordService;
import com.ada.genealogyapp.user.dto.UserLoginRequest;
import com.ada.genealogyapp.user.dto.UserLoginResponse;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.service.UserSearchService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Primary
@RequiredArgsConstructor
class JwtAuthService implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserSearchService userSearchService;
    private final PasswordService passwordService;
    private final JwtUtil jwtUtil;


    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {

        Try.of(() -> {
            User user = userSearchService.findUserByUsernameOrThrowInvalidCredentials(userLoginRequest.getUsername());
            passwordService.match(userLoginRequest.getPassword(), user.getPassword());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            user = (User) authentication.getPrincipal();
            log.info("Authentication successful for user: {}", user.getUsername());
            String token = jwtUtil.generateToken(user.getUsername(), user.getAuthorities());
            log.info("JWT token generated successfully for user: {}", user.getUsername());
            return new UserLoginResponse(user.getUsername(), token, user.getId(), user.getRole());
        }).onFailure(throwable -> {
            log.error("Login failed for user: {}", userLoginRequest.getUsername());
            throw new InvalidCredentialsException("Login failed for user: " + userLoginRequest.getUsername());
        });
        return null;
    }
}
