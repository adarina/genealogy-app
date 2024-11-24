package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.relationship.RelationshipManager;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FamilyMotherManagementService {

    private final PersonService personService;

    private final TreeService treeService;

    private final FamilyService familyService;

    private final RelationshipManager relationshipManager;

    public FamilyMotherManagementService(PersonService personService, TreeService treeService, FamilyService familyService, RelationshipManager relationshipManager) {
        this.personService = personService;
        this.treeService = treeService;
        this.familyService = familyService;
        this.relationshipManager = relationshipManager;
    }

    @TransactionalInNeo4j
    public void addMotherToFamily(UUID treeId, UUID familyId, UUID motherId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person mother = personService.findPersonById(motherId);

        if (family.hasMother(mother)) {
            throw new NodeAlreadyInNodeException("Mother " + motherId + "is already part of the family: " + familyId);
        }
        if (nonNull(family.getMother())) {
            removeMotherFromFamily(treeId, familyId, motherId);
        }

        family.addMother(mother);
        family.setName((nonNull(family.getFather()) ? family.getFather().getName() : "null") + " & " + mother.getName());
        familyService.saveFamily(family);

        for (Person child : family.getChildren()) {
            relationshipManager.addParentChildRelationship(mother, child, PersonRelationshipType.BIOLOGICAL);
        }
        log.info("Mother {} added successfully to the family {}", motherId, familyId);
    }

    @TransactionalInNeo4j
    public void removeMotherFromFamily(UUID treeId, UUID familyId, UUID motherId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person mother = personService.findPersonById(motherId);

        family.removeMother();
        for (Person child : family.getChildren()) {
            relationshipManager.removeParentChildRelationship(mother, child);
        }
        family.setName((nonNull(family.getFather()) ? family.getFather().getName() : "null") + " & null");

        familyService.saveFamily(family);
        log.info("Mother {} removed from family {}", mother, familyId);
    }
}
