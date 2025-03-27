package com.ada.genealogyapp.person.validation;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GenderPersonValidator extends Validator<Person> {

    private final FieldValidationService fieldValidationService;

    @Override
    public void check(Person person, ValidationResult result) {
        fieldValidationService.validate(FieldType.ENUM_GENDER_FIELD, person.getGender(), result);
        checkNext(person, result);
    }
}