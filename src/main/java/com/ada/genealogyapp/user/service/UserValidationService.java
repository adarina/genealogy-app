package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.validation.*;
import com.ada.genealogyapp.validation.factory.DefaultFieldValidationFactory;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserValidationService {
    private final Validator<User> validator;

    public UserValidationService() {
        FieldValidationService fieldValidationService = new FieldValidationService(new DefaultFieldValidationFactory());
        this.validator = Validator.link(
                new UsernameUserValidator(fieldValidationService),
                new LastnameUserValidator(fieldValidationService),
                new PasswordUserValidator(fieldValidationService),
                new PhoneUserValidator(fieldValidationService),
                new FirstnameUserValidator(fieldValidationService)
        );
    }

    public void validateUser(User user) {
        ValidationResult result = new ValidationResult();
        validator.check(user, result);
        if (result.hasErrors()) {
            log.error("User validation failed for user {}: {}", user.getId(), result.getErrors());
            throw new ValidationException("User validation failed: " + result.getErrors());
        }
        log.info("User validation succeeded for citation: {}", user.getId());
    }
}
