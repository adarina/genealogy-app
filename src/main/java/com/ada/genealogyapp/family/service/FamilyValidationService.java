package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.validation.StatusFamilyValidator;
import com.ada.genealogyapp.validation.factory.DefaultFieldValidationFactory;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FamilyValidationService {
    private final Validator<Family> validator;

    public FamilyValidationService() {
        FieldValidationService fieldValidationService = new FieldValidationService(new DefaultFieldValidationFactory());
        this.validator = Validator.link(
            new StatusFamilyValidator(fieldValidationService)
        );
    }

    public void validateFamily(Family family) {
        ValidationResult result = new ValidationResult();
        validator.check(family, result);

        if (result.hasErrors()) {
            log.error("Family validation failed for family {}: {}", family.getId(), result.getErrors());
            throw new ValidationException("Family validation failed: " + result.getErrors());
        }

        log.info("Family validation succeeded for family: {}", family.getId());
    }
}
