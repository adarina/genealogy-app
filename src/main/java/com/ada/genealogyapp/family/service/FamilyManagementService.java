package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventSearchService;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FamilyManagementService {


    private final FamilySearchService familySearchService;

    private final FamilyRepository familyRepository;

    private final TreeSearchService treeSearchService;

    private final PersonSearchService personSearchService;

    private final EventSearchService eventSearchService;

    public FamilyManagementService(FamilySearchService familySearchService, FamilyRepository familyRepository, TreeSearchService treeSearchService, PersonSearchService personSearchService, EventSearchService eventSearchService) {
        this.familySearchService = familySearchService;
        this.familyRepository = familyRepository;
        this.treeSearchService = treeSearchService;
        this.personSearchService = personSearchService;
        this.eventSearchService = eventSearchService;
    }

    public void updateFamily(UUID treeId, UUID familyId, FamilyRequest familyRequest) {
        treeSearchService.findTreeById(treeId);
        Family existingFamily = familySearchService.findFamilyById(familyId);

        Function<UUID, Person> personFinder = personSearchService::findPersonById;
        Function<UUID, Event> eventFinder = eventSearchService::findEventById;

        updateFather(existingFamily, familyRequest, personFinder);
        updateMother(existingFamily, familyRequest, personFinder);
        updateChildren(existingFamily, familyRequest, personFinder);
        updateEvents(existingFamily, familyRequest, eventFinder);

        Family updatedFamily = familyRepository.save(existingFamily);
        log.info("Family updated successfully: {}", updatedFamily.getId());
    }

    private void updateFather(Family existingFamily, FamilyRequest familyRequest, Function<UUID, Person> personFinder) {
        UUID newFatherId = familyRequest.getFatherId();
        if (existingFamily.getFather() != null && existingFamily.getFather().getId().equals(newFatherId)) {
            existingFamily.setFather(null);
        } else if (newFatherId != null) {
            Person newFather = personFinder.apply(newFatherId);
            existingFamily.setFather(newFather);
        }
    }

    private void updateMother(Family existingFamily, FamilyRequest familyRequest, Function<UUID, Person> personFinder) {
        UUID newMotherId = familyRequest.getMotherId();
        if (existingFamily.getMother() != null && existingFamily.getMother().getId().equals(newMotherId)) {
            existingFamily.setMother(null);
        } else if (newMotherId != null) {
            Person newMother = personFinder.apply(newMotherId);
            existingFamily.setMother(newMother);
        }
    }

    private void updateChildren(Family existingFamily, FamilyRequest familyRequest, Function<UUID, Person> personFinder) {
        if (!familyRequest.getChildrenIds().isEmpty()) {
            Set<Person> childrenToRemove = existingFamily.getChildren().stream()
                    .filter(child -> !familyRequest.getChildrenIds().contains(child.getId()))
                    .collect(Collectors.toSet());
            existingFamily.getChildren().removeAll(childrenToRemove);

            Set<Person> newChildren = familyRequest.getChildrenIds().stream()
                    .map(personFinder)
                    .filter(child -> !existingFamily.getChildren().stream()
                            .map(Person::getId)
                            .collect(Collectors.toSet())
                            .contains(child.getId()))
                    .collect(Collectors.toSet());
            existingFamily.getChildren().addAll(newChildren);
        }
    }

    private void updateEvents(Family existingFamily, FamilyRequest familyRequest, Function<UUID, Event> eventFinder) {
        if (!familyRequest.getEventsIds().isEmpty()) {
            Set<Event> eventsToRemove = existingFamily.getEvents().stream()
                    .filter(event -> !familyRequest.getEventsIds().contains(event.getId()))
                    .collect(Collectors.toSet());
            existingFamily.getEvents().removeAll(eventsToRemove);

            Set<Event> newEvents = familyRequest.getEventsIds().stream()
                    .map(eventFinder)
                    .filter(event -> !existingFamily.getEvents().stream()
                            .map(Event::getId)
                            .collect(Collectors.toSet())
                            .contains(event.getId()))
                    .collect(Collectors.toSet());
            existingFamily.getEvents().addAll(newEvents);
        }
    }
}