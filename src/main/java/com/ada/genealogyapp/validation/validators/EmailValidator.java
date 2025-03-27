package com.ada.genealogyapp.validation.validators;

import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidator;

import static java.util.Objects.nonNull;

public class EmailValidator implements FieldValidator<String> {
    @Override
    public void validate(String value, ValidationResult result) {
        if (nonNull(value) && !value.matches("^(.+)@(.+)$")) {
            result.addError(value + " is not email");
        }
    }
}
