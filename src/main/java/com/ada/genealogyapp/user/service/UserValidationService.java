package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserValidationService {
    private final UserValidator userValidator;

    public UserValidationService() {
        this.userValidator = UserValidator.link(
                new UsernameUserValidator(),
                new LastnameUserValidator(),
                new PasswordUserValidator(),
                new PhoneUserValidator(),
                new FirstnameUserValidator()
        );
    }

    public boolean validateUser(User user) {
        boolean isValid = userValidator.check(user);
        if (!isValid) {
            log.error("User validation failed: {}", user);
        } else {
            log.info("User validation passed: {}", user);
        }
        return isValid;
    }
}
