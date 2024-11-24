package com.ada.genealogyapp.family.validation;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.user.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StatusFamilyValidator extends FamilyValidator {

    @Override
    public void check(Family family, ValidationResult result) {
        if (family.getStatus() == null) {
            log.error("Family validation failed: Status is null");
            result.addError("Status is null");
        }
        checkNext(family, result);
    }
}