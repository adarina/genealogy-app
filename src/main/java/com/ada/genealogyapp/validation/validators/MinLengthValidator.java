package com.ada.genealogyapp.validation.validators;


import com.ada.genealogyapp.validation.service.FieldValidator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@Slf4j
public class MinLengthValidator implements FieldValidator<String> {
    private final int minLength;

    public MinLengthValidator(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public void validate(String value, ValidationResult result) {
        if (nonNull(value) && value.length() < minLength) {
            result.addError(value + " is shorter than the minimum length of " + minLength);
        }
    }
}