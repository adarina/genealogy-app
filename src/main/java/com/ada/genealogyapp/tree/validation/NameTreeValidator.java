package com.ada.genealogyapp.tree.validation;

import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.validation.model.Validator;
import com.ada.genealogyapp.validation.result.ValidationResult;
import com.ada.genealogyapp.validation.service.FieldValidationService;
import com.ada.genealogyapp.validation.type.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NameTreeValidator extends Validator<Tree> {

    private final FieldValidationService fieldValidationService;

    @Override
    public void check(Tree tree, ValidationResult result) {
        fieldValidationService.validate(FieldType.TREE_SHORT_TEXT_FIELD, tree.getName(), result);
        checkNext(tree, result);
    }
}