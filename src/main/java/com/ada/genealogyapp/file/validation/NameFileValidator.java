package com.ada.genealogyapp.file.validation;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class NameFileValidator extends Validator<File> {

    private final FieldValidationService fieldValidationService;

    @Override
    public void check(File file, ValidationResult result) {
        fieldValidationService.validate(FieldType.TREE_LONG_TEXT_FIELD, file.getName(), result);
        checkNext(file, result);
    }
}