package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class FamilyManagementService {


    private final FamilyService familyService;

    private final TreeService treeService;

    private final PersonSearchService personSearchService;

    private final EventService eventService;

    public FamilyManagementService(FamilyService familyService, TreeService treeService, PersonSearchService personSearchService, EventService eventService) {
        this.familyService = familyService;
        this.treeService = treeService;
        this.personSearchService = personSearchService;
        this.eventService = eventService;
    }


    public void updateFamily(UUID treeId, UUID familyId, FamilyRequest familyRequest) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        Family family = familyService.findFamilyByIdOrThrowNodeNotFoundException(familyId);

        updateFather(family, familyRequest);
        updateMother(family, familyRequest);
        updateChildren(family, familyRequest);
        updateEvents(family, familyRequest);

        familyService.saveFamily(family);
        log.info("Family updated successfully: {}", family.getId());
    }

    private void updateFather(Family family, FamilyRequest familyRequest) {
        UUID newFatherId = familyRequest.getFatherId();
        if (nonNull(family.getFather()) && family.getFather().getId().equals(newFatherId)) {
            family.setFather(null);
        } else if (nonNull(newFatherId)) {
            Person newFather = personSearchService.findPersonById(newFatherId);
            family.setFather(newFather);
        }
    }

    private void updateMother(Family family, FamilyRequest familyRequest) {
        UUID newMotherId = familyRequest.getMotherId();
        if (nonNull(family.getMother()) && family.getMother().getId().equals(newMotherId)) {
            family.setMother(null);
        } else if (nonNull(newMotherId)) {
            Person newMother = personSearchService.findPersonById(newMotherId);
            family.setMother(newMother);
        }
    }

    private void updateChildren(Family family, FamilyRequest familyRequest) {
        if (!familyRequest.getChildrenIds().isEmpty()) {
            Set<UUID> currentChildrenIds = new HashSet<>();
            for (Person child : family.getChildren()) {
                currentChildrenIds.add(child.getId());
            }
            List<Person> childrenToRemove = new ArrayList<>();
            for (Person child : family.getChildren()) {
                if (!familyRequest.getChildrenIds().contains(child.getId())) {
                    childrenToRemove.add(child);
                }
            }
            childrenToRemove.forEach(family.getChildren()::remove);
            for (UUID childId : familyRequest.getChildrenIds()) {
                if (!currentChildrenIds.contains(childId)) {
                    Person newChild = personSearchService.findPersonById(childId);
                    family.getChildren().add(newChild);
                }
            }
        }
    }

    private void updateEvents(Family existingFamily, FamilyRequest familyRequest) {
        if (!familyRequest.getEventsIds().isEmpty()) {
            Set<UUID> currentEventIds = new HashSet<>();
            for (Event event : existingFamily.getEvents()) {
                currentEventIds.add(event.getId());
            }
            List<Event> eventsToRemove = new ArrayList<>();
            for (Event event : existingFamily.getEvents()) {
                if (!familyRequest.getEventsIds().contains(event.getId())) {
                    eventsToRemove.add(event);
                }
            }
            eventsToRemove.forEach(existingFamily.getEvents()::remove);
            for (UUID eventId : familyRequest.getEventsIds()) {
                if (!currentEventIds.contains(eventId)) {
                    Event newEvent = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);
                    existingFamily.getEvents().add(newEvent);
                }
            }
        }
    }
}