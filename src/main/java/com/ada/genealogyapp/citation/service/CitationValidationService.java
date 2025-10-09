package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.validation.DateCitationValidator;
import com.ada.genealogyapp.citation.validation.PageCitationValidator;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.result.ValidationResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitationValidationService {

    private Validator<Citation> validator;

    private final FieldValidationService fieldValidationService;

    @PostConstruct
    public void init() {
        validator = Validator.link(
                new PageCitationValidator(fieldValidationService),
                new DateCitationValidator(fieldValidationService)
        );
    }

    public void validateCitation(Citation citation) {
        ValidationResult result = new ValidationResult();
        validator.check(citation, result);
        if (result.hasErrors()) {
            log.error("Citation validation failed for citation {}: {}", citation.getId(), result.getErrors());
            throw new ValidationException("Citation validation failed: " + result.getErrors());
        }
    }
}
