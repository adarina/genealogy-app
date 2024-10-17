package com.ada.genealogyapp.user.controller;

import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.service.UserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/manage")
public class UserManagementController {

    private final UserManagementService userService;

    public UserManagementController(UserManagementService userService) {
        this.userService = userService;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteRequest userDeleteRequest) {
        userService.deleteUserByUsername(userDeleteRequest.getUsername());
        return ResponseEntity.accepted().build();
    }
}

