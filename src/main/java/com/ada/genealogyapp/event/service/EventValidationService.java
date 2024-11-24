package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.validation.DescriptionEventValidator;
import com.ada.genealogyapp.event.validation.EventValidator;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.user.validation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class EventValidationService {
    private final EventValidator eventValidator;

    public EventValidationService() {
        this.eventValidator = EventValidator.link(
                new DescriptionEventValidator()
        );
    }

    public void validateEvent(Event event) {
        ValidationResult result = new ValidationResult();
        eventValidator.check(event, result);

        if (result.hasErrors()) {
            log.error("Event validation failed for event {}: {}", event.getId(), result.getErrors());
            throw new ValidationException("Event validation failed: " + result.getErrors());
        }

        log.info("Event validation succeeded for event: {}", event.getId());
    }
}