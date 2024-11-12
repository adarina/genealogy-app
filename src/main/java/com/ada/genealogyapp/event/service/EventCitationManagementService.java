package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationManagementService;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class EventCitationManagementService {


    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final CitationService citationService;

    private final CitationManagementService citationManagementService;

    public EventCitationManagementService(TreeTransactionService treeTransactionService, EventManagementService eventManagementService, CitationService citationService, CitationManagementService citationManagementService) {
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.citationService = citationService;
        this.citationManagementService = citationManagementService;
    }

    @TransactionalInNeo4j
    public void removeCitationFromEvent(UUID treeId, UUID eventId, UUID citationId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = eventManagementService.validateTreeAndEvent(treeId, eventId);
        Citation citation = citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);

        String cypher = "MATCH (E:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (e)-[r:HAS_EVENT_CITATION]->(c) " +
                "DELETE r";

        tx.run(cypher, Map.of(
                "eventId", event.getId().toString(),
                "citationId", citation.getId().toString()));
        log.info("Citation {} removed from event {}", citation.getId(), event.getId());
        tx.commit();
    }

    @TransactionalInNeo4j
    public void updateEventCitation(UUID treeId, UUID eventId, UUID citationId, EventCitationRequest eventCitationRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Citation citation = eventManagementService.validateTreeEventAndCitation(treeId, eventId, citationId);

        citationManagementService.updatePage(tx, citation.getId(), eventCitationRequest.getPage());
        citationManagementService.updateDate(tx, citation.getId(), eventCitationRequest.getDate());

        log.info("Updated citation {} for event {} ", citationId, eventId);
        tx.commit();
    }
}
