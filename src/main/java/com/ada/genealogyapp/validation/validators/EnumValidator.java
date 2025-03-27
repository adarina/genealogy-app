package com.ada.genealogyapp.validation.validators;

import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidator;

public class EnumValidator<T extends Enum<T>> implements FieldValidator<T> {
    private final Class<T> enumClass;

    public EnumValidator(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void validate(T value, ValidationResult result) {
        if (value == null) {
            result.addError("Value is null");
        } else if (!isEnumValue(value)) {
            result.addError("Value " + value + " is not a valid enum value for " + enumClass.getSimpleName());
        }
    }

    private boolean isEnumValue(T value) {
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
