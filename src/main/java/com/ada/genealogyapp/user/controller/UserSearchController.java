package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.UserResponse;
import com.ada.genealogyapp.user.dto.UsersResponse;
import com.ada.genealogyapp.user.service.UserSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/users")
public class UserSearchController {

    private final UserSearchService userSearchService;

    public UserSearchController(UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    @GetMapping("/all")
    public ResponseEntity<UsersResponse> getUsers() {
        return ResponseEntity.ok(userSearchService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userSearchService.find(id)
                .map(value -> ResponseEntity.ok(UserResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
