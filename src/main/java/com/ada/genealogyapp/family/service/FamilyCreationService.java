package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.service.EventSearchService;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.family.relationship.ChildRelationship;
import com.ada.genealogyapp.family.type.ChildRelationshipType;
import com.ada.genealogyapp.family.relationship.FamilyRelationship;
import com.ada.genealogyapp.family.type.FamilyRelationshipType;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

import static java.util.Objects.nonNull;


@Service
@Slf4j
public class FamilyCreationService {

    private final FamilyService familyService;

    private final PersonSearchService personSearchService;

    private final EventSearchService eventSearchService;

    private final TreeService treeService;

    public FamilyCreationService(FamilyService familyService, PersonSearchService personSearchService, EventSearchService eventSearchService, TreeService treeService) {
        this.familyService = familyService;
        this.personSearchService = personSearchService;
        this.eventSearchService = eventSearchService;
        this.treeService = treeService;
    }


    public void createFamily(UUID treeId, FamilyRequest familyRequest) {
        Family family = mapToFamily(familyRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        family.setFamilyTree(tree);

        handlePartnerRelationships(family);
        handleChildRelationships(family);

        familyService.saveFamily(family);
        treeService.saveTree(tree);

        log.info("Family created successfully: {}", family.getId());
    }

    private void handlePartnerRelationships(Family family) {
        Person father = family.getFather();
        Person mother = family.getMother();

        if (nonNull(father) && nonNull(mother)) {
            addPartnerRelationship(father, mother);
        }
    }

    private void addPartnerRelationship(Person father, Person mother) {
        FamilyRelationship partnerRelationship = new FamilyRelationship(mother, FamilyRelationshipType.UNKNOWN);
        father.getPartners().add(partnerRelationship);

        FamilyRelationship reversePartnerRelationship = new FamilyRelationship(father, FamilyRelationshipType.UNKNOWN);
        mother.getPartners().add(reversePartnerRelationship);
    }

    private void handleChildRelationships(Family family) {
        Set<Person> children = family.getChildren();
        Person father = family.getFather();
        Person mother = family.getMother();

        for (Person child : children) {
            if (nonNull(father)) {
                addChildRelationship(father, child);
            }
            if (nonNull(mother)) {
                addChildRelationship(mother, child);
            }
        }
    }

    private void addChildRelationship(Person parent, Person child) {
        ChildRelationship childRelationship = new ChildRelationship(child, ChildRelationshipType.BIOLOGICAL);
        parent.getChildren().add(childRelationship);
    }


    public Family mapToFamily(FamilyRequest request) {
        return Family.builder()
                .father(personSearchService.findPersonById(request.getFatherId()))
                .mother(personSearchService.findPersonById(request.getMotherId()))
                .children(personSearchService.findPersonsByIds(request.getChildrenIds()))
                .events(eventSearchService.findEventsByIds(request.getEventsIds()))
                .build();
    }
}
