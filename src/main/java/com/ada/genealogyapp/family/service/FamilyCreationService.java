package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyCreationService {

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;

    @TransactionalInNeo4j
    public Family createFamily(String userId, String treeId, FamilyRequest familyRequest) {

        Family family = Family.builder()
                .status(familyRequest.getStatus())
                .name("null & null")
                .build();

        familyValidationService.validateFamily(family);
        familyService.saveFamily(userId, treeId, family);
        return family;
    }

    @TransactionalInNeo4j
    public Family createFamily(String userId, Tree tree, StatusType status) {

        Family family = Family.builder()
                .status(status)
                .tree(tree)
                .name("null & null")
                .build();

        familyValidationService.validateFamily(family);
        familyService.saveFamily(userId, tree.getId(), family);

        return family;
    }
}