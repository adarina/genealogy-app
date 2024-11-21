package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.person.service.RelationshipManager;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventCitationManagementService {


    private final CitationService citationService;

    private final TreeService treeService;

    private final EventService eventService;

    private final RelationshipManager relationshipManager;

    public EventCitationManagementService(CitationService citationService, TreeService treeService, EventService eventService, RelationshipManager relationshipManager) {
        this.citationService = citationService;
        this.treeService = treeService;
        this.eventService = eventService;
        this.relationshipManager = relationshipManager;
    }

    @TransactionalInNeo4j
    public void removeCitationFromEvent(UUID treeId, UUID eventId, UUID citationId) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);
        Citation citation = citationService.findCitationById(citationId);

        relationshipManager.removeEventCitationRelationship(event, citation);
        log.info("Citation {} removed from event {}", citationId, eventId);
    }

    @TransactionalInNeo4j
    public void addCitationToEvent(UUID treeId, UUID eventId, UUID citationId) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);
        Citation citation = citationService.findCitationById(citationId);

        if (event.isCitationAlreadyInEvent(citationId)) {
            throw new NodeAlreadyInNodeException("Citation " + citationId + " is already in the event " + eventId);
        }

        relationshipManager.addEventCitationRelationship(event, citation);
        log.info("Citation {} added successfully to the event {}", citationId, eventId);
    }
}
