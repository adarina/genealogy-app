package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.validation.FirstnamePersonValidator;
import com.ada.genealogyapp.person.validation.GenderPersonValidator;
import com.ada.genealogyapp.person.validation.LastnamePersonValidator;
import com.ada.genealogyapp.validation.factory.DefaultFieldValidationFactory;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonValidationService {
    private final Validator<Person> validator;

    public PersonValidationService() {
        FieldValidationService fieldValidationService = new FieldValidationService(new DefaultFieldValidationFactory());
        this.validator = Validator.link(
                new FirstnamePersonValidator(fieldValidationService),
                new LastnamePersonValidator(fieldValidationService),
                new GenderPersonValidator(fieldValidationService)
        );
    }

    public void validatePerson(Person person) {
        ValidationResult result = new ValidationResult();
        validator.check(person, result);

        if (result.hasErrors()) {
            log.error("Person validation failed for person {}: {}", person.getId(), result.getErrors());
            throw new ValidationException("Person validation failed: " + result.getErrors());
        }
        log.info("Person validation succeeded for event: {}", person.getId());
    }
}