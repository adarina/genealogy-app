package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.service.RelationshipManager;
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

    private final RelationshipManager relationshipManager;

    //TODO validation
    @TransactionalInNeo4j
    public void updateEventInFamily(UUID treeId, UUID familyId, UUID eventId, ParticipantEventRequest participantEventRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Event event = eventService.findEventById(eventId);

        relationshipManager.updateEventParticipantRelationship(event, family, participantEventRequest.getRelationship());
        log.info("Event {} relationship updated in family {}", eventId, familyId);
    }

    @TransactionalInNeo4j
    public void removeFamilyFromEvent(UUID treeId, UUID familyId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Event event = eventService.findEventById(eventId);

        relationshipManager.removeEventParticipantRelationship(event, family);
        log.info("Family {} removed from event {}", familyId, eventId);
    }

    @TransactionalInNeo4j
    public void addFamilyToEvent(UUID treeId, UUID familyId, UUID eventId, ParticipantEventRequest participantEventRequest) {
        treeService.ensureTreeExists(treeId);
        Family family = familyService.findFamilyById(familyId);
        Event event = eventService.findEventById(eventId);

        if (event.isFamilyAlreadyInEvent(familyId)) {
            throw new NodeAlreadyInNodeException("Family " + familyId + " is already a participant of the event " + eventId);
        }

        relationshipManager.addEventParticipantRelationship(event, family, participantEventRequest.getRelationship());
        log.info("Family {} added successfully to the event {}", familyId, eventId);
    }
}
