package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyEventManagementService {

    private final EventService eventService;

    private final TreeService treeService;

    private final FamilyService familyService;


    @TransactionalInNeo4j
    public void removeFamilyFromEvent(UUID treeId, UUID familyId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Event event = eventService.findEventById(eventId);

        event.getParticipants().removeIf(rel -> rel.getParticipant().equals(family));
        eventService.saveEvent(event);
        log.info("Family {} removed from event {}", familyId, eventId);
    }
}
