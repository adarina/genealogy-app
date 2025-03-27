package com.ada.genealogyapp.event.validation;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TypeEventValidator extends Validator<Event> {

    private final FieldValidationService fieldValidationService;

    @Override
    public void check(Event event, ValidationResult result) {
        fieldValidationService.validate(FieldType.ENUM_TYPE_FIELD, event.getType(), result);
        checkNext(event, result);
    }
}