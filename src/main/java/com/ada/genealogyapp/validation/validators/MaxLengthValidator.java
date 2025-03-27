package com.ada.genealogyapp.validation.validators;

import com.ada.genealogyapp.validation.service.FieldValidator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@Slf4j
public class MaxLengthValidator implements FieldValidator<String> {
    private final int maxLength;

    public MaxLengthValidator(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void validate(String value, ValidationResult result) {
        if (nonNull(value) && value.length() > maxLength) {
            result.addError(value + " exceeds the maximum length of " + maxLength);
        }
    }
}