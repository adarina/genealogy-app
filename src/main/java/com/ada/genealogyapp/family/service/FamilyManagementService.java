package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class FamilyManagementService {

    private final TreeService treeService;

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;

    public FamilyManagementService(TreeService treeService, FamilyService familyService, FamilyValidationService familyValidationService) {
        this.treeService = treeService;
        this.familyService = familyService;
        this.familyValidationService = familyValidationService;
    }


    @TransactionalInNeo4j
    public void updateFamily(UUID treeId, UUID familyId, FamilyRequest familyRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);

        family.setStatus(familyRequest.getStatus());

        familyValidationService.validateFamily(family);

        familyService.saveFamily(family);
    }

    @TransactionalInNeo4j
    public void deleteFamily(UUID treeId, UUID familyId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);

        familyService.deleteFamily(family);
    }
}
