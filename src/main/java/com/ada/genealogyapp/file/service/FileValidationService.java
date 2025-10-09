package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.validation.NameFileValidator;
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
public class FileValidationService {
    private Validator<File> validator;

    private final FieldValidationService fieldValidationService;

    @PostConstruct
    public void init() {
        validator = Validator.link(
                new NameFileValidator(fieldValidationService)
        );
    }

    public void validateFile(File file) {
        ValidationResult result = new ValidationResult();
        validator.check(file, result);

        if (result.hasErrors()) {
            log.error("File validation failed for file {}: {}", file.getId(), result.getErrors());
            throw new ValidationException("File validation failed: " + result.getErrors());
        }
    }
}
