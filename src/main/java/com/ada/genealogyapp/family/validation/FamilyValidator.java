package com.ada.genealogyapp.family.validation;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.validation.ValidationResult;

import static java.util.Objects.nonNull;

public abstract class FamilyValidator {

    private FamilyValidator next;

    public static FamilyValidator link(FamilyValidator first, FamilyValidator... chain) {
        FamilyValidator head = first;
        for (FamilyValidator nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract void check(Family family, ValidationResult result);

    protected void checkNext(Family family, ValidationResult result) {
        if (nonNull(next)) {
            next.check(family, result);
        }
    }
}