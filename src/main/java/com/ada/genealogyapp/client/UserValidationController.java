package com.ada.genealogyapp.client;

import com.ada.genealogyapp.user.dto.UserRequest;
import com.ada.genealogyapp.user.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/graph-users")
@RequiredArgsConstructor
public class UserValidationController {

    private final UserValidationService userValidationService;


    @PostMapping("/validate")
    public ResponseEntity<Void> validateUser(@RequestBody UserRequest user) {
        userValidationService.validateUser(user);
        return ResponseEntity.ok().build();
    }
}
