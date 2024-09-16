package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/manage")
public class UserManagementController {

    private final UserService userService;

    public UserManagementController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest) {
        boolean isDeleted = userService.deleteUserByUsername(userDeleteRequest.getUsername());
        if (isDeleted) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

