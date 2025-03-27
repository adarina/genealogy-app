package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.UserResponse;
import com.ada.genealogyapp.user.dto.UsersResponse;
import com.ada.genealogyapp.user.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/users")
public class UserSearchController {

    private final UserSearchService userSearchService;

    @GetMapping("/all")
    public ResponseEntity<UsersResponse> getUsers() {
        return ResponseEntity.ok(userSearchService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userSearchService.findUserById(id)
                .map(value -> ResponseEntity.ok(UserResponse.entityToDtoMapper().apply(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
