package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.auth.AuthService;
import com.ada.genealogyapp.user.dto.UserLoginRequest;
import com.ada.genealogyapp.user.dto.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/genealogy/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping
    public ResponseEntity<UserLoginResponse> authenticate(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(authService.login(userLoginRequest));
    }
}
