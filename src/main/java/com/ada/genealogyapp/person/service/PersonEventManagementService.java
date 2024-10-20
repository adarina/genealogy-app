package com.ada.genealogyapp.person.service;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PersonEventManagementService {

    private final PersonManagementService personManagementService;

    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final CitationService citationService;

    public PersonEventManagementService(PersonManagementService personManagementService, TreeTransactionService treeTransactionService, EventManagementService eventManagementService, CitationService citationService) {
        this.personManagementService = personManagementService;
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.citationService = citationService;
    }


    @Transactional
    public void updatePersonalEvent(UUID treeId, UUID personId, UUID eventId, EventRequest eventRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Event event = personManagementService.validateTreePersonAndEvent(treeId, personId, eventId);

        eventManagementService.updateEventType(tx, event.getId(), eventRequest.getType());
        eventManagementService.updateDate(tx, event.getId(), eventRequest.getDate());
        eventManagementService.updatePlace(tx, event.getId(), eventRequest.getPlace());
        eventManagementService.updateDescription(tx, event.getId(), eventRequest.getDescription());

        if (nonNull(eventRequest.getEventRelationshipType())) {
            String cypher = "MATCH (p:Person {id: $personId}) " +
                    "MATCH (p)-[r:HAS_EVENT]->(e:Event {id: $eventId}) " +
                    "SET r.eventRelationshipType = $relationshipType";

            tx.run(cypher, Map.of("personId", personId, "eventId", event.getId().toString(), "relationshipType", eventRequest.getEventRelationshipType().name()));

            log.info("Updated event {} in person {}", event.getId(), personId);
        }
    }

    @Transactional
    public void addSourceToPersonalEvent(UUID treeId, UUID personId, UUID eventId, UUID citationId) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Event event = personManagementService.validateTreePersonAndEvent(treeId, personId, eventId);

        Citation citation = citationService.findCitationById(citationId);
        if (event.getCitations().contains(citation)) {
            throw new NodeAlreadyInNodeException("Citation " + citation.getId() + " is already part of the personal event " + eventId);
        }
        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (e)-[:HAS_CITATION]->(c)";

        tx.run(cypher, Map.of("eventId", eventId.toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the personal event {}", citation.getId(), event.getId().toString());
    }
}

