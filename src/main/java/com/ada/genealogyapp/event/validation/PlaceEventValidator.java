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
public class PlaceEventValidator extends Validator<Event> {

    private final FieldValidationService validationService;

    @Override
    public void check(Event event, ValidationResult result) {
        validationService.validate(FieldType.TREE_MEDIUM_TEXT_FIELD, event.getPlace(), result);
        checkNext(event, result);
    }
}