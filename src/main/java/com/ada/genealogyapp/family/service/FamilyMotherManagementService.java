package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FamilyMotherManagementService {

    private final FamilyService familyService;


    public FamilyMotherManagementService(FamilyService familyService) {
        this.familyService = familyService;
    }

    @TransactionalInNeo4j
    public void addMotherToFamily(String userId, String treeId, String familyId, String personId) {
        familyService.addMotherToFamily(userId, treeId, familyId, personId);
    }

    @TransactionalInNeo4j
    public void removeMotherFromFamily(String userId, String treeId, String familyId, String motherId) {
        familyService.removeMotherFromFamily(userId, treeId, familyId, motherId);
    }
}
