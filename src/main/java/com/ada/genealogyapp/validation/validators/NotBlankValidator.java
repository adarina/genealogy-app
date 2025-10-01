package com.ada.genealogyapp.validation.validators;

import com.ada.genealogyapp.validation.service.FieldValidator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;


@Slf4j
public class NotBlankValidator implements FieldValidator<String> {
    @Override
    public void validate(String value, ValidationResult result) {
        if (isNull(value)) {
            result.addError("Value is null");
        } else if (value.isBlank()) {
            result.addError("Value is blank");
        }
    }
}
