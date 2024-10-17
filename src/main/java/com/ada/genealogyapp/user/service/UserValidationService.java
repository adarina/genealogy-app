package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.exceptions.UserValidationException;
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

    public ValidationResult validateUser(User user) {
        ValidationResult validationResult = new ValidationResult();
        userValidator.check(user, validationResult);
        if (!validationResult.getErrors().isEmpty()) {
            log.error("User validation failed for user {}: {}", user.getUsername(), validationResult.getErrors());
        } else {
            log.info("User validation passed for user: {}", user.getUsername());
        }
        return validationResult;
    }

    public void validateUserOrThrowUserValidationException(User user) {
        ValidationResult validationResult = validateUser(user);
        if (!validationResult.getErrors().isEmpty()) {
            log.error("User validation failed for registration: {}", validationResult.getErrors());
            throw new UserValidationException("User validation failed for registration: " + validationResult.getErrors());
        } else {
            log.error("User validation succeeded for registration");
        }
    }
}
