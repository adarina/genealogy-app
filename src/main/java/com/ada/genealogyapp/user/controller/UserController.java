package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.service.UserAuthenticationService;
import com.ada.genealogyapp.user.service.UserRegistrationService;
import com.ada.genealogyapp.user.service.UserSearchService;
import com.ada.genealogyapp.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy")
public class UserController {

    private final UserService userService;

    private final UserSearchService userSearchService;

    private final UserAuthenticationService userAuthenticationService;

    private final UserRegistrationService userRegistrationService;

    public UserController(UserService userService, UserSearchService userSearchService, UserAuthenticationService userAuthenticationService, UserRegistrationService userRegistrationService) {
        this.userService = userService;
        this.userSearchService = userSearchService;
        this.userAuthenticationService = userAuthenticationService;
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        boolean isRegistered = userRegistrationService.registerUser(userRequest);
        if (isRegistered) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userAuthenticationService.loginUser(loginRequest));
    }

    @GetMapping("/all")
    public ResponseEntity<UsersResponse> getUsers() {
        return ResponseEntity.ok(userSearchService.getAllUsers());
    }

    @GetMapping("/all/id")
    public ResponseEntity<UserResponse> getUser(@RequestBody GetRequest getRequest) {
        return userSearchService.find(getRequest.getId())
                .map(value -> ResponseEntity.ok(UserResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        boolean isDeleted = userService.deleteUserByUsername(deleteRequest.getUsername());
        if (isDeleted) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

