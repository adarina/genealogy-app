package com.ada.genealogyapp.validation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Predicate;

import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
public class FieldValidator<T> {

    private final String fieldName;

    private final Predicate<T> validationRule;

    private final String errorMessage;

    private final boolean checkForBlank;

    private final int minLength;

    private final int maxLength;


    public void validate(T value, ValidationResult result) {
        if (checkForBlank && (value == null || value.toString().isBlank())) {
            log.error("Validation failed for {}: field is blank or null", fieldName);
            result.addError(fieldName + " is blank or null");
        } else if (value != null && value.toString().length() < minLength) {
            log.error("Validation failed for {}: value is shorter than the minimum length of {}", fieldName, minLength);
            result.addError(fieldName + " is shorter than the minimum length of " + minLength);
        } else if (value != null && value.toString().length() > maxLength) {
            log.error("Validation failed for {}: value exceeds the maximum length of {}", fieldName, maxLength);
            result.addError(fieldName + " exceeds the maximum length of " + maxLength);
        } else if (!validationRule.test(value)) {
            log.error("Validation failed for {}: {}", fieldName, errorMessage);
            result.addError(errorMessage);
        }
    }

    public static FieldValidator<String> forString(String fieldName, String regex, String errorMessage, boolean checkForBlank, int minLength, int maxLength) {
        return new FieldValidator<>(fieldName, value -> nonNull(value) && value.matches(regex), errorMessage, checkForBlank, minLength, maxLength);
    }
}
