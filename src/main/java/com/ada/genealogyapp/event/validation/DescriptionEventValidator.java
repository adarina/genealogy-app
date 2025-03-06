package com.ada.genealogyapp.event.validation;

import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidatorFactory;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DescriptionEventValidator extends EventValidator {

    private static final FieldValidator<String> DESCRIPTION_VALIDATOR = ValidatorFactory.createStringValidator(
            "description",
            ".*",
            false,
            0,
            300
    );

    @Override
    public void check(Event event, ValidationResult result) {
        DESCRIPTION_VALIDATOR.validate(event.getDescription(), result);
        checkNext(event, result);
    }
}

