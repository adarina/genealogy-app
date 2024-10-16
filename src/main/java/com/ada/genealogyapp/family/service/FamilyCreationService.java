package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventSearchService;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.relationship.ChildRelationship;
import com.ada.genealogyapp.person.relationship.ChildRelationshipType;
import com.ada.genealogyapp.person.relationship.FamilyRelationship;
import com.ada.genealogyapp.person.relationship.FamilyRelationshipType;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class FamilyCreationService {

    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    private final FamilyRepository familyRepository;

    private final PersonSearchService personSearchService;

    private final EventSearchService eventSearchService;

    public FamilyCreationService(TreeSearchService treeSearchService, TreeRepository treeRepository, FamilyRepository familyRepository, PersonSearchService personSearchService, EventSearchService eventSearchService) {
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
        this.familyRepository = familyRepository;
        this.personSearchService = personSearchService;
        this.eventSearchService = eventSearchService;
    }


    public Family create(Family family) {
        Family savedFamily = familyRepository.save(family);
        log.info("Family created successfully: {}", savedFamily);
        return savedFamily;
    }

    public void createFamily(UUID treeId, FamilyRequest familyRequest) {
        Function<UUID, Person> personFinder = personSearchService::findPersonById;
        Function<UUID, Event> eventFinder = eventSearchService::findEventById;

        Family family = FamilyRequest.dtoToEntityMapper(personFinder, eventFinder).apply(familyRequest);


        Tree tree = treeSearchService.findTreeById(treeId);
        family.setFamilyTree(tree);

        Person father = family.getFather();
        Person mother = family.getMother();
        Set<Person> children = family.getChildren();

        if (father != null && mother != null) {
            FamilyRelationship partnerRelationship = new FamilyRelationship(mother, FamilyRelationshipType.UNKNOWN); // Domyślnie UNKNOWN
            father.getPartners().add(partnerRelationship);

            FamilyRelationship reversePartnerRelationship = new FamilyRelationship(father, FamilyRelationshipType.UNKNOWN);
            mother.getPartners().add(reversePartnerRelationship);
        }


        for (Person child : children) {
            if (father != null) {
                ChildRelationship fatherChildRelationship = new ChildRelationship(child, ChildRelationshipType.BIOLOGICAL); // Dziecko jako TargetNode
                father.getChildren().add(fatherChildRelationship);
            }
            if (mother != null) {
                ChildRelationship motherChildRelationship = new ChildRelationship(child, ChildRelationshipType.BIOLOGICAL); // Dziecko jako TargetNode
                mother.getChildren().add(motherChildRelationship);
            }
        }


        create(family);
        treeRepository.save(tree);

        log.info("Family created successfully: {}", family.getId());
    }
}
