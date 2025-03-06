package com.ada.genealogyapp.family.validation;


import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class StatusFamilyValidator extends FamilyValidator {

    private static final FieldValidator<String> STATUS_VALIDATOR = ValidatorFactory.createStringValidator(
            "status",
            ".*",
            true,
            0,
            300
    );

    @Override
    public void check(Family family, ValidationResult result) {
        STATUS_VALIDATOR.validate(family.getStatus().name(), result);
        checkNext(family, result);
    }
}