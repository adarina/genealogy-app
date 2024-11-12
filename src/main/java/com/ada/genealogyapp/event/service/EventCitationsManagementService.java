package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
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
        eventManagementService.validateTreeAndEvent(treeId, eventId);
        Citation citation = citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);

        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (e)-[:HAS_EVENT_CITATION]->(c)";

        tx.run(cypher, Map.of(
                "eventId", eventId.toString(),
                "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the event {}", citation.getPage(), eventId);
        tx.commit();
    }
}