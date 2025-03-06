package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.validation.FirstnamePersonValidator;
import com.ada.genealogyapp.person.validation.PersonValidator;
import com.ada.genealogyapp.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PersonValidationService {
    private final PersonValidator personValidator;

    public PersonValidationService() {
        this.personValidator = PersonValidator.link(
                new FirstnamePersonValidator()
        );
    }

    public void validatePerson(Person person) {
        ValidationResult result = new ValidationResult();
        personValidator.check(person, result);

        if (result.hasErrors()) {
            log.error("Person validation failed for person {}: {}", person.getId(), result.getErrors());
            throw new ValidationException("Person validation failed: " + result.getErrors());
        }
    }
}