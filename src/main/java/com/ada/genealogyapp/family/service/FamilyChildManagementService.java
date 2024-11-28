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


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyChildManagementService {

    private final PersonService personService;

    private final TreeService treeService;

    private final FamilyService familyService;

    private final RelationshipManager relationshipManager;

    @TransactionalInNeo4j
    public void addChildToFamily(String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person child = personService.findPersonById(childId);

        if (family.hasChild(child)) {
            throw new NodeAlreadyInNodeException("Child " + childId + " is already part of the family " + familyId);
        }

        family.addChild(child);
        familyService.saveFamily(family);

        relationshipManager.updateParentChildRelationships(family, child, familyChildRequest);

        log.info("Child {} added successfully to the family {}", childId, familyId);
    }

    @TransactionalInNeo4j
    public void updateChildInFamily(String treeId, String familyId, String childId, FamilyChildRequest familyChildRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person child = personService.findPersonById(childId);

        relationshipManager.updateParentChildRelationships(family, child, familyChildRequest);

        log.info("Child {} relationships updated in family {}", childId, familyId);
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(String treeId, String familyId, String childId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Person child = personService.findPersonById(childId);

        family.getChildren().remove(child);
        familyService.saveFamily(family);

        relationshipManager.removeParentChildRelationships(family, child);

        log.info("Child {} removed from family {}", childId, familyId);
    }

//    private void updateParentChildRelationships(Family family, Person child, FamilyChildRequest familyChildRequest) {
//        updateParentChildRelationship(family.getFather(), child, familyChildRequest.getFatherRelationship());
//        updateParentChildRelationship(family.getMother(), child, familyChildRequest.getMotherRelationship());
//    }
//
//    private void updateParentChildRelationship(Person parent, Person child, PersonRelationshipType relationship) {
//        if (nonNull(parent)) {
//            parent.getRelationships().removeIf(rel -> rel.getChild().getId().equals(child.getId()));
//            if (relationship != null) {
//                PersonRelationship personRelationship = PersonRelationship.builder()
//                        .child(child)
//                        .relationship(relationship)
//                        .build();
//                parent.getRelationships().add(personRelationship);
//            }
//            personService.savePerson(parent);
//        }
//    }

//    private void removeParentChildRelationships(Family family, Person child) {
//        if (nonNull(family.getFather())) {
//            family.getFather().getRelationships().removeIf(rel -> rel.getChild().getId().equals(child.getId()));
//            personService.savePerson(family.getFather());
//        }
//        if (nonNull(family.getMother())) {
//            family.getMother().getRelationships().removeIf(rel -> rel.getChild().getId().equals(child.getId()));
//            personService.savePerson(family.getMother());
//        }
//    }
}