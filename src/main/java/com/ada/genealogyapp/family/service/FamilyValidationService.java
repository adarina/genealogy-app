package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.validation.FamilyValidator;
import com.ada.genealogyapp.family.validation.StatusFamilyValidator;
import com.ada.genealogyapp.user.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FamilyValidationService {
    private final FamilyValidator familyValidator;

    public FamilyValidationService() {
        this.familyValidator = FamilyValidator.link(
            new StatusFamilyValidator()
        );
    }

    public void validateFamily(Family family) {
        ValidationResult result = new ValidationResult();
        familyValidator.check(family, result);

        if (result.hasErrors()) {
            log.error("Family validation failed for family {}: {}", family.getId(), result.getErrors());
            throw new ValidationException("Family validation failed: " + result.getErrors());
        }

        log.info("Family validation succeeded for family: {}", family.getId());
    }
}
