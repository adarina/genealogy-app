package com.ada.genealogyapp.citation.validation;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PageCitationValidator extends CitationValidator {

    private static final FieldValidator<String> PAGE_VALIDATOR = ValidatorFactory.createStringValidator(
            "page",
            ".*",
            false,
            0,
            300
    );

    @Override
    public void check(Citation citation, ValidationResult result) {
        PAGE_VALIDATOR.validate(citation.getPage(), result);
        checkNext(citation, result);
    }
}