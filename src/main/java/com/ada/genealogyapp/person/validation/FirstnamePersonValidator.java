package com.ada.genealogyapp.person.validation;

import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.validation.ValidationResult;
import com.ada.genealogyapp.validation.FieldValidator;
import com.ada.genealogyapp.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FirstnamePersonValidator extends PersonValidator {

    private static final FieldValidator<String> FIRSTNAME_VALIDATOR = ValidatorFactory.createStringValidator(
            "firstname",
            ".*",
            false,
            0,
            100
    );

    @Override
    public void check(Person person, ValidationResult result) {
        FIRSTNAME_VALIDATOR.validate(person.getFirstname(), result);
        checkNext(person, result);
    }
}