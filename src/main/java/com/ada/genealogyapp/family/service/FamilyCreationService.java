package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyCreationService {

    private final TreeService treeService;

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;

    @TransactionalInNeo4j
    public Family createFamily(UUID treeId, FamilyRequest familyRequest) {
        Tree tree = treeService.findTreeById(treeId);

        Family family = Family.builder()
                .tree(tree)
                .status(familyRequest.getStatus())
                .name("null & null")
                .build();

        familyValidationService.validateFamily(family);

        familyService.saveFamily(family);
        log.info("Family created successfully: {}", family);
        return family;
    }
}