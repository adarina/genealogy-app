package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.validation.DateEventValidator;
import com.ada.genealogyapp.event.validation.DescriptionEventValidator;
import com.ada.genealogyapp.event.validation.PlaceEventValidator;
import com.ada.genealogyapp.event.validation.TypeEventValidator;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.validation.factory.DefaultFieldValidationFactory;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventValidationService {
    private final Validator<Event> validator;

    public EventValidationService() {
        FieldValidationService fieldValidationService = new FieldValidationService(new DefaultFieldValidationFactory());
        this.validator = Validator.link(
                new DescriptionEventValidator(fieldValidationService),
                new DateEventValidator(fieldValidationService),
                new PlaceEventValidator(fieldValidationService),
                new TypeEventValidator(fieldValidationService)
        );
    }

    public void validateEvent(Event event) {
        ValidationResult result = new ValidationResult();
        validator.check(event, result);
        if (result.hasErrors()) {
            log.error("Event validation failed for event {}: {}", event.getId(), result.getErrors());
            throw new ValidationException("Event validation failed: " + result.getErrors());
        }
        log.info("Event validation succeeded for event: {}", event.getId());
    }
}