package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FamilyManagementService {

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;

    public FamilyManagementService(FamilyService familyService, FamilyValidationService familyValidationService) {
        this.familyService = familyService;
        this.familyValidationService = familyValidationService;
    }


    @TransactionalInNeo4j
    public void updateFamily(String userId, String treeId, String familyId, FamilyRequest familyRequest) {
        Family family = Family.builder()
                .status(familyRequest.getStatus())
                .build();

        familyValidationService.validateFamily(family);
        familyService.updateFamily(userId, treeId, familyId, family);
    }

    @TransactionalInNeo4j
    public void deleteFamily(String userId, String treeId, String familyId) {
        familyService.deleteFamily(userId, treeId, familyId);
    }
}
