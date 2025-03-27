package com.ada.genealogyapp.validation.validators;

import com.ada.genealogyapp.validation.service.FieldValidator;
import com.ada.genealogyapp.validation.result.ValidationResult;

import static java.util.Objects.nonNull;

public class NumericValidator implements FieldValidator<String> {
    @Override
    public void validate(String value, ValidationResult result) {
        if (nonNull(value) && !value.matches("\\d{9}")) {
            result.addError(value + " is not numeric");
        }
    }
}