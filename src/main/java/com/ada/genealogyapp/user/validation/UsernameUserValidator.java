package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UsernameUserValidator extends UserValidator {

    private static final FieldValidator<String> USERNAME_VALIDATOR = ValidatorFactory.createStringValidator(
            "username",
            "^(.+)@(.+)$",
            true,
            0,
            100
    );

    @Override
    public void check(User user, ValidationResult result) {
        USERNAME_VALIDATOR.validate(user.getUsername(), result);
        checkNext(user, result);
    }
}


