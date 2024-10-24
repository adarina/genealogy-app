package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class EventCitationsManagementService {

    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final CitationService citationService;

    public EventCitationsManagementService(TreeTransactionService treeTransactionService, EventManagementService eventManagementService, CitationService citationService) {
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.citationService = citationService;
    }


    @TransactionalInNeo4j
    public void addExistingCitationToEvent(UUID treeId, UUID eventId, UUID citationId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = eventManagementService.validateTreeAndEvent(treeId, eventId);
        Citation citation = citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);

        if (event.getCitations().contains(citation)) {
            throw new NodeAlreadyInNodeException("Citation " + citation.getId() + " is already part of the event " + eventId);
        }
        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (e)-[:HAS_CITATION]->(c)";

        tx.run(cypher, Map.of("eventId", eventId.toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the event {}", citation.getPage(), event.getId());
        tx.commit();
    }

    @TransactionalInNeo4j
    public void removeCitationFromEvent(UUID treeId, UUID eventId, EventCitationRequest eventCitationRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = eventManagementService.validateTreeAndEvent(treeId, eventId);
        Citation citation = citationService.findCitationByIdOrThrowNodeNotFoundException(eventCitationRequest.getId());

        String cypher = "MATCH (E:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (e)-[r:HAS_CITATION]->(c) " +
                "DELETE r";

        tx.run(cypher, Map.of("eventId", event.getId().toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} removed from event {}", citation.getId(), event.getId());
        tx.commit();
    }
}
