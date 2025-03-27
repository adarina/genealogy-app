package com.ada.genealogyapp.citation.validation;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class PageCitationValidator extends Validator<Citation> {

    private final FieldValidationService validationService;

    @Override
    public void check(Citation citation, ValidationResult result) {
        validationService.validate(FieldType.TREE_SHORT_TEXT_FIELD, citation.getPage(), result);
        checkNext(citation, result);
    }
}