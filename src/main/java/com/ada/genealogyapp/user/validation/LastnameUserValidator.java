package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LastnameUserValidator extends UserValidator {

    private static final FieldValidator<String> LASTNAME_VALIDATOR = ValidatorFactory.createStringValidator(
            "lastname",
            "[A-Za-z]+",
            true,
            1,
            100
    );

    @Override
    public void check(User user, ValidationResult result) {
        LASTNAME_VALIDATOR.validate(user.getLastname(), result);
        checkNext(user, result);
    }
}
