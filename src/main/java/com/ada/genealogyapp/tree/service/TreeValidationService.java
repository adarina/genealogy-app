package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.validation.NameTreeValidator;
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
public class TreeValidationService {
    private  Validator<Tree> validator;

    private final FieldValidationService fieldValidationService;

    @PostConstruct
    public void init() {
        validator = Validator.link(
                new NameTreeValidator(fieldValidationService)
        );
    }

    public void validateTree(Tree tree) {
        ValidationResult result = new ValidationResult();
        validator.check(tree, result);

        if (result.hasErrors()) {
            log.error("Tree validation failed for tree {}: {}", tree.getId(), result.getErrors());
            throw new ValidationException("Tree validation failed: " + result.getErrors());
        }
        log.info("Tree validation succeeded for tree: {}", tree.getId());
    }
}
