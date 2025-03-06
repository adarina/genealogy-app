package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PasswordUserValidator extends UserValidator {

    private static final FieldValidator<String> PASSWORD_VALIDATOR = ValidatorFactory.createStringValidator(
            "password",
            ".*",
            true,
            8,
            20
    );

    @Override
    public void check(User user, ValidationResult result) {
        PASSWORD_VALIDATOR.validate(user.getPassword(), result);
        checkNext(user, result);
    }
}
