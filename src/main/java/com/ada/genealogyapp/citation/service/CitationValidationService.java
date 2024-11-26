package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.validation.CitationValidator;
import com.ada.genealogyapp.citation.validation.PageCitationValidator;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.user.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CitationValidationService {

    private final CitationValidator citationValidator;

    public CitationValidationService() {
        this.citationValidator = CitationValidator.link(
                new PageCitationValidator()
        );
    }

    public void validateCitation(Citation citation) {
        ValidationResult result = new ValidationResult();
        citationValidator.check(citation, result);

        if (result.hasErrors()) {
            log.error("Citation validation failed for citation {}: {}", citation.getId(), result.getErrors());
            throw new ValidationException("Citation validation failed: " + result.getErrors());
        }

        log.info("Citation validation succeeded for citation: {}", citation.getId());
    }
}
