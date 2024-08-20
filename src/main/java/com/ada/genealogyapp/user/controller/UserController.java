package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("api/v1/genealogy")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<GetUsersResponse> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request, UriComponentsBuilder builder) {
        return userService.registerUser(request, builder);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUserById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody CreateLoginRequest createLoginRequest) {
        return userService.loginUser(createLoginRequest);
    }
}

