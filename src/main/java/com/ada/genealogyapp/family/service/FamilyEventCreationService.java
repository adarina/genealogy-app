package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FamilyEventCreationService {

    private final FamilyService familyService;

    private final EventService eventService;

    private final TreeService treeService;

    public FamilyEventCreationService(FamilyService familyService, EventService eventService, TreeService treeService) {
        this.familyService = familyService;
        this.eventService = eventService;
        this.treeService = treeService;
    }


    public void createFamilyEvent(UUID treeId, UUID familyId, EventRequest eventRequest) {
        Event event = EventRequest.dtoToEntityMapper().apply(eventRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        Family family = familyService.findFamilyByIdOrThrowNodeNotFoundException(familyId);

        eventService.checkEventApplicable(event, "Family");

        event.setTree(tree);
        family.getEvents().add(event);

        eventService.saveEvent(event);
        familyService.saveFamily(family);

        log.info("Successfully created event {} for family {}", event.getId(), family.getId());
    }
}
