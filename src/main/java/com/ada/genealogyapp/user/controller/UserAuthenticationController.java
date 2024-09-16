package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.UserLoginRequest;
import com.ada.genealogyapp.user.service.UserAuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/genealogy/login")
public class UserAuthenticationController {

    private final UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        return ResponseEntity.ok(userAuthenticationService.loginUser(userLoginRequest));
    }
}
