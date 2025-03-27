package com.ada.genealogyapp.validation.service;

import com.ada.genealogyapp.validation.factory.FieldValidationFactory;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FieldValidationService {

    private final FieldValidationFactory<FieldType, FieldValidator<?>> factory;

    public <T> void validate(FieldType fieldType, T value, ValidationResult result) {
        List<FieldValidator<?>> validators = factory.getValidators(fieldType);
        for (FieldValidator<?> validator : validators) {
            FieldValidator<T> typedValidator = (FieldValidator<T>) validator;
            typedValidator.validate(value, result);
        }
    }
}
