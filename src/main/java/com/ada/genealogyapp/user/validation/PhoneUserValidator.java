package com.ada.genealogyapp.user.validation;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class PhoneUserValidator extends Validator<User> {

    private final FieldValidationService validationService;

    @Override
    public void check(User user, ValidationResult result) {
        validationService.validate(FieldType.PHONE_NUMERIC_FIELD, user.getPhone(), result);
        checkNext(user, result);
    }
}
