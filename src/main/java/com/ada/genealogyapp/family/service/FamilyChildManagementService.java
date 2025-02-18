package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyChildManagementService {

    private final FamilyService familyService;

    @TransactionalInNeo4j
    public void addChildToFamily(String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest) {
        familyService.addChildToFamily(treeId, familyId, childId, familyChildRequest.getFatherRelationship().name(), familyChildRequest.getMotherRelationship().name());
    }

    @TransactionalInNeo4j
    public void updateChildInFamily(String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest) {
        familyService.updateChildInFamily(treeId, familyId, childId, familyChildRequest);
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(String treeId, String familyId, String childId) {
        familyService.removeChildFromFamily(treeId, familyId, childId);
    }
}