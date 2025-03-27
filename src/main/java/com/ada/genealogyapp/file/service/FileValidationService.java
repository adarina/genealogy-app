package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.validation.NameFileValidator;
import com.ada.genealogyapp.validation.factory.DefaultFieldValidationFactory;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FileValidationService {
    private final Validator<File> validator;

    public FileValidationService() {
        FieldValidationService fieldValidationService = new FieldValidationService(new DefaultFieldValidationFactory());
        this.validator = Validator.link(
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
        log.info("File validation succeeded for file: {}", file.getId());
    }
}