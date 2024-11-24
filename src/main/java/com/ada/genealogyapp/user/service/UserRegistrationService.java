package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserRegistrationService {
    private final UserManagementService userService;

    private final UserSearchService userSearchService;

    private final UserValidationService userValidationService;

    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserManagementService userService, UserSearchService userSearchService, UserValidationService userValidationService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userSearchService = userSearchService;
        this.userValidationService = userValidationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(UserRequest request) {
        User user = UserRequest.dtoToEntityMapper().apply(request);

        userSearchService.findUserByUsernameOrThrowUserAlreadyExistsException(user.getUsername());
        userValidationService.validateUserOrThrowUserValidationException(user);

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        userService.saveUser(user);

        log.info("User registered successfully: {}", user.getUsername());

    }
}
