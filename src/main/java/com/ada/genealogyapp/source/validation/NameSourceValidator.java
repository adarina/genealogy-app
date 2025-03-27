package com.ada.genealogyapp.source.validation;

import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NameSourceValidator extends Validator<Source> {

    private final FieldValidationService fieldValidationService;

    @Override
    public void check(Source source, ValidationResult result) {
        fieldValidationService.validate(FieldType.TREE_SHORT_TEXT_FIELD, source.getName(), result);
        checkNext(source, result);
    }
}