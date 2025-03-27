package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.graphuser.service.GraphUserCreationService;
import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserManagementService userService;

    private final UserSearchService userSearchService;

    private final UserValidationService userValidationService;

    private final PasswordEncoder passwordEncoder;

    private final GraphUserCreationService graphUserCreationService;



    @Transactional
    public void registerUser(UserRequest request) {
        User user = UserRequest.dtoToEntityMapper().apply(request);

        userSearchService.findUserByUsernameOrThrowUserAlreadyExistsException(user.getUsername());
        userValidationService.validateUser(user);

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        userService.saveUser(user);
        graphUserCreationService.createGraphUser(String.valueOf(user.getId()));

        log.info("User registered successfully: {}", user.getUsername());

    }
}
