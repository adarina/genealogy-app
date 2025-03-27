package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.UserRequest;
import com.ada.genealogyapp.user.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/register")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        userRegistrationService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}