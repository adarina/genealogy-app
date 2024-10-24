package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class FamilyEventManagementService {

    private final FamilyManagementService familyManagementService;

    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final CitationService citationService;

    public FamilyEventManagementService(FamilyManagementService familyManagementService, TreeTransactionService treeTransactionService, EventManagementService eventManagementService, CitationService citationService) {
        this.familyManagementService = familyManagementService;
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.citationService = citationService;
    }

    @TransactionalInNeo4j
    public void updateFamilyEvent(UUID treeId, UUID familyId, UUID eventId, EventRequest eventRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = familyManagementService.validateTreeFamilyAndEvent(treeId, familyId, eventId);

        eventManagementService.updateEventType(tx, event.getId(), eventRequest.getType());
        eventManagementService.updateDate(tx, event.getId(), eventRequest.getDate());
        eventManagementService.updatePlace(tx, event.getId(), eventRequest.getPlace());
        eventManagementService.updateDescription(tx, event.getId(), eventRequest.getDescription());

        if (nonNull(eventRequest.getEventRelationshipType())) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (f)-[r:HAS_EVENT]->(e:Event {id: $eventId}) " +
                    "SET r.eventRelationshipType = $relationshipType";

            tx.run(cypher, Map.of("familyId", familyId, "eventId", event.getId().toString(), "relationshipType", eventRequest.getEventRelationshipType().name()));

            log.info("Updated event {} in family {}", event.getId(), familyId);
        }
        tx.commit();
    }

    @TransactionalInNeo4j
    public void addCitationToFamilyEvent(UUID treeId, UUID familyId, UUID eventId, UUID citationId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = familyManagementService.validateTreeFamilyAndEvent(treeId, familyId, eventId);

        Citation citation = citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);
        if (event.getCitations().contains(citation)) {
            throw new NodeAlreadyInNodeException("Citation " + citation.getId() + " is already part of the family event " + eventId);
        }
        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (e)-[:HAS_CITATION]->(c)";

        tx.run(cypher, Map.of("eventId", eventId.toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the family event {}", citation.getId(), event.getId().toString());
        tx.commit();
    }
}
