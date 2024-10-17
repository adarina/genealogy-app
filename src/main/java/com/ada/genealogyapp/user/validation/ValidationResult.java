package com.ada.genealogyapp.user.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationResult {

    private final List<String> errors = new ArrayList<>();

    public void addError(String error) {
        errors.add(error);
    }
}
