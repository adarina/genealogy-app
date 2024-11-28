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

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FamilyFatherManagementService {

    private final PersonService personService;

    private final TreeService treeService;

    private final FamilyService familyService;

    private final RelationshipManager relationshipManager;

    public FamilyFatherManagementService(PersonService personService, TreeService treeService, FamilyService familyService, RelationshipManager relationshipManager) {
        this.personService = personService;
        this.treeService = treeService;
        this.familyService = familyService;
        this.relationshipManager = relationshipManager;
    }

    @TransactionalInNeo4j
    public void addFatherToFamily(String treeId, String familyId, String fatherId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person father = personService.findPersonById(fatherId);

        if (family.hasFather(father)) {
            throw new NodeAlreadyInNodeException("Father " + fatherId + "is already part of the family: " + familyId);
        }
        if (nonNull(family.getFather())) {
            removeFatherFromFamily(treeId, familyId, fatherId);
        }

        family.addFather(father);
        family.setName(father.getName() + " & " + (nonNull(family.getMother()) ? family.getMother().getName() : "null"));
        familyService.saveFamily(family);

        for (Person child : family.getChildren()) {
            relationshipManager.addParentChildRelationship(father, child, PersonRelationshipType.BIOLOGICAL);
        }
        log.info("Father {} added successfully to the family {}", fatherId, familyId);
    }

    @TransactionalInNeo4j
    public void removeFatherFromFamily(String treeId, String familyId, String fatherId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person father = personService.findPersonById(fatherId);

        family.removeFather();
        for (Person child : family.getChildren()) {
            relationshipManager.removeParentChildRelationship(father, child);
        }
        family.setName("null & " + (nonNull(family.getMother()) ? family.getMother().getName() : "null"));

        familyService.saveFamily(family);
        log.info("Father {} removed from family {}", father, familyId);
    }
}
