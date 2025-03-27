package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.validation.DateCitationValidator;
import com.ada.genealogyapp.citation.validation.PageCitationValidator;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.validation.factory.DefaultFieldValidationFactory;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.result.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CitationValidationService {

    private final Validator<Citation> validator;

    public CitationValidationService() {
        FieldValidationService fieldValidationService = new FieldValidationService(new DefaultFieldValidationFactory());
        this.validator = Validator.link(
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
        log.info("Citation validation succeeded for citation: {}", citation.getId());
    }
}
