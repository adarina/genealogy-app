package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.relationship.RelationshipManager;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyChildManagementService {

    private final PersonService personService;

    private final TreeService treeService;

    private final FamilyService familyService;

    private final RelationshipManager relationshipManager;

    @TransactionalInNeo4j
    public void addChildToFamily(UUID treeId, UUID familyId, UUID childId, FamilyChildRequest familyChildRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person child = personService.findPersonById(childId);

        if (family.hasChild(child)) {
            throw new NodeAlreadyInNodeException("Child " + childId + " is already part of the family " + familyId);
        }

        family.addChild(child);
        familyService.saveFamily(family);

        relationshipManager.addParentChildRelationships(family, child, familyChildRequest.getFatherRelationship(), familyChildRequest.getMotherRelationship());
        log.info("Child {} added successfully to the family {}", childId, familyId);
    }

    //TODO validation
    @TransactionalInNeo4j
    public void updateChildInFamily(UUID treeId, UUID familyId, UUID childId, FamilyChildRequest familyChildRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person child = personService.findPersonById(childId);

        relationshipManager.updateParentChildRelationships(family, child, familyChildRequest);
        log.info("Child {} relationships updated in family {}", childId, familyId);
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(UUID treeId, UUID familyId, UUID childId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person child = personService.findPersonById(childId);

        family.removeChild(child);
        relationshipManager.removeParentChildRelationships(family, child);
        familyService.saveFamily(family);

        log.info("Child {} removed from family {}", childId, familyId);
    }
}