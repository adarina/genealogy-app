package com.ada.genealogyapp.validation.factory;

import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.validation.service.FieldValidator;
import com.ada.genealogyapp.validation.type.FieldType;
import com.ada.genealogyapp.validation.validators.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultFieldValidationFactory implements FieldValidationFactory<FieldType, FieldValidator<?>> {

    private final Map<FieldType, List<FieldValidator<?>>> validatorsMap = Map.ofEntries(
            Map.entry(FieldType.TEXT_FIELD, List.of(new NotBlankValidator(), new MinLengthValidator(3))),
            Map.entry(FieldType.NUMERIC_FIELD, List.of(new NumericValidator(), new NotBlankValidator())),
            Map.entry(FieldType.ENUM_STATUS_FIELD, List.of(new EnumValidator<>(StatusType.class))),
            Map.entry(FieldType.ENUM_TYPE_FIELD, List.of(new EnumValidator<>(EventType.class))),
            Map.entry(FieldType.ENUM_GENDER_FIELD, List.of(new EnumValidator<>(GenderType.class))),
            Map.entry(FieldType.TREE_SHORT_TEXT_FIELD, List.of(new MaxLengthValidator(300))),
            Map.entry(FieldType.TREE_MEDIUM_TEXT_FIELD, List.of(new MaxLengthValidator(1000))),
            Map.entry(FieldType.TREE_LONG_TEXT_FIELD, List.of(new MaxLengthValidator(5000))),
            Map.entry(FieldType.USER_NAME_TEXT_FIELD, List.of(new NotBlankValidator(), new MinLengthValidator(1), new MaxLengthValidator(50))),
            Map.entry(FieldType.PASSWORD_TEXT_FIELD, List.of(new NotBlankValidator(), new MinLengthValidator(6), new MaxLengthValidator(50))),
            Map.entry(FieldType.EMAIL_TEXT_FIELD, List.of(new NotBlankValidator(), new MaxLengthValidator(50), new EmailValidator())),
            Map.entry(FieldType.TREE_DATE_FIELD, List.of(new MaxLengthValidator(30), new DateValidator())),
            Map.entry(FieldType.PHONE_NUMERIC_FIELD, List.of(new NotBlankValidator(), new MaxLengthValidator(15), new NumericValidator()))
    );

    @Override
    public List<FieldValidator<?>> getValidators(FieldType fieldType) {
        return validatorsMap.getOrDefault(fieldType, List.of());
    }
}
