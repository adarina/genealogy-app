package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.validation.NameSourceValidator;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceValidationService {
    private Validator<Source> validator;

    private final FieldValidationService fieldValidationService;

    @PostConstruct
    public void init() {
        validator = Validator.link(
                new NameSourceValidator(fieldValidationService)
        );
    }

    public void validateSource(Source source) {
        ValidationResult result = new ValidationResult();
        validator.check(source, result);

        if (result.hasErrors()) {
            log.error("Source validation failed for source {}: {}", source.getId(), result.getErrors());
            throw new ValidationException("Source validation failed: " + result.getErrors());
        }
        log.info("Source validation succeeded for source: {}", source.getId());
    }
}
