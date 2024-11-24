package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
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

    private final FamilyRepository familyRepository;

    public FamilyManagementService(TreeService treeService, FamilyService familyService, FamilyRepository familyRepository) {
        this.treeService = treeService;
        this.familyService = familyService;
        this.familyRepository = familyRepository;
    }

    //TODO validation
    @TransactionalInNeo4j
    public void updateFamily(UUID treeId, UUID familyId, FamilyRequest familyRequest) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);

        familyRepository.updateFamily(familyId, familyRequest.getStatus());
        log.info("Family updated successfully: {}", familyId);
    }

    @TransactionalInNeo4j
    public void deleteFamily(UUID treeId, UUID familyId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);

        familyService.deleteFamily(family);
    }
}
