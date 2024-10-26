package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.family.type.RelationshipRelationshipType;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class FamilyCreationService {

    private final TreeService treeService;

    private final FamilyRepository familyRepository;

    public FamilyCreationService(TreeService treeService, FamilyRepository familyRepository) {
        this.treeService = treeService;
        this.familyRepository = familyRepository;
    }

    @TransactionalInNeo4j
    public void createFamily(UUID treeId, FamilyRequest familyRequest) {
        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);

        Family family = new Family();
        family.setFamilyTree(tree);

        if (familyRequest.getRelationshipRelationshipType() == null) {
            family.setRelationshipRelationshipType(RelationshipRelationshipType.UNKNOWN);
        } else {
            family.setRelationshipRelationshipType(familyRequest.getRelationshipRelationshipType());
        }
        family.setName("null & null");

        familyRepository.save(family);
        treeService.saveTree(tree);

        log.info("Family created successfully: {}", family.getId());
    }
}
