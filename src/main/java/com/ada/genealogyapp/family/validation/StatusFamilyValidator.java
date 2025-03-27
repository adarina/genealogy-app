package com.ada.genealogyapp.family.validation;


import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class StatusFamilyValidator extends Validator<Family> {

    private final FieldValidationService fieldValidationService;

    @Override
    public void check(Family family, ValidationResult result) {
        fieldValidationService.validate(FieldType.ENUM_STATUS_FIELD, family.getStatus(), result);
        checkNext(family, result);
    }
}