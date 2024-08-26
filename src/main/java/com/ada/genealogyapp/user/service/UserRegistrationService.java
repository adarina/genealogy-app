package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.exceptions.UserAlreadyExistsException;
import com.ada.genealogyapp.exceptions.UserValidationException;
import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRegistrationService {
    private final UserService userService;

    private final UserSearchService userSearchService;

    private final UserValidationService userValidationService;

    public UserRegistrationService(UserService userService, UserSearchService userSearchService, UserValidationService userValidationService) {
        this.userService = userService;
        this.userSearchService = userSearchService;
        this.userValidationService = userValidationService;
    }

    public boolean registerUser(UserRequest request) {
        User user = UserRequest.dtoToEntityMapper().apply(request);

        if (userSearchService.find(user.getUsername()).isPresent()) {
            log.error("User with username {} already exists", user.getUsername());
            throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }

        if (!userValidationService.validateUser(user)) {
            log.error("User validation failed for registration: {}", user.getUsername());
            throw new UserValidationException("User validation failed for registration: " + user.getUsername());
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user = userService.create(user);
        log.info("User registered successfully: {}", user.getUsername());

        return true;
    }
}
