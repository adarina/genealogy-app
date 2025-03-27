package com.ada.genealogyapp.validation.service;


import com.ada.genealogyapp.validation.result.ValidationResult;

public interface FieldValidator<T> {
    void validate(T value, ValidationResult result);
}
