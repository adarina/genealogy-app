package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PhoneUserValidator extends UserValidator {

    private static final FieldValidator<String> PHONE_VALIDATOR = ValidatorFactory.createStringValidator(
            "phone",
            "\\d{9}",
            false,
            0,
            100
    );

    @Override
    public void check(User user, ValidationResult result) {
        PHONE_VALIDATOR.validate(user.getPhone(), result);
        checkNext(user, result);
    }
}
