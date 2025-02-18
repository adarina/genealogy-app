package com.ada.genealogyapp;

public class ValidatorFactory {

    public static FieldValidator<String> createStringValidator(String fieldName, String regex, boolean checkForBlank, int minLength, int maxLength) {
        return FieldValidator.forString(
                fieldName,
                regex,
                fieldName + " is invalid",
                checkForBlank,
                minLength,
                maxLength
        );
    }
}
