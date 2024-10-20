package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.event.service.EventServiceImpl;
import com.ada.genealogyapp.event.type.EventRelationshipType;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FamilyEventsManagementService {

    private final TreeTransactionService treeTransactionService;
    private final FamilyManagementService familyManagementService;
    private final EventServiceImpl eventService;

    public FamilyEventsManagementService(TreeTransactionService treeTransactionService, FamilyManagementService familyManagementService, EventServiceImpl eventService) {
        this.treeTransactionService = treeTransactionService;
        this.familyManagementService = familyManagementService;
        this.eventService = eventService;
    }

    @Transactional
    public void addEventToFamily(UUID treeId, UUID familyId, UUID eventId, EventRelationshipType eventRelationshipType) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Event event = eventService.findEventById(eventId);

        Set<EventRelationship> eventRelationships = family.getEvents();
        for (EventRelationship relationship : eventRelationships) {
            if (relationship.getEvent().getId().equals(event.getId())) {
                throw new NodeAlreadyInNodeException("Event " + event.getId() + " is already part of the family " + familyId);
            }
        }

        EventRelationshipType type = nonNull(eventRelationshipType) ? eventRelationshipType : EventRelationshipType.FAMILY;
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (c:Event {id: $eventId}) " +
                "MERGE (f)-[:HAS_EVENT {eventRelationshipType: $relationshipType}]->(c)";

        tx.run(cypher, Map.of("familyId", familyId.toString(), "eventId", event.getId().toString(), "relationshipType", type.name()));
        log.info("Event {} added successfully to the family {}", event.getId(), family.getId());

        addDefaultHasParticipantToEvent(tx, family, event);
    }

    @Transactional
    public void addDefaultHasParticipantToEvent(Transaction tx, Family family, Event event) {

        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (f:Family {id: $familyId}) " +
                "SET f:Participant " +
                "MERGE (e)-[:HAS_PARTICIPANT]->(f)";

        tx.run(cypher, Map.of("familyId", family.getId().toString(), "eventId", event.getId().toString()));
        log.info("Added family {} as participant to event {}", family.getId(), event.getId());
    }

    @Transactional
    public void removeEventFromFamily(UUID treeId, UUID familyId, UUIDRequest UUIDRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Event event = eventService.findEventById(UUIDRequest.getId());

        removeEventFromFamily(family, tx, event);
    }

    @Transactional
    public void removeEventFromFamily(Family family, Transaction tx, Event event) {
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (e:Event {id: $eventId}) " +
                "MATCH (f)-[r1:HAS_EVENT]->(e) " +
                "MATCH (e)-[r2:HAS_PARTICIPANT]->(f) " +
                "DELETE r1, r2 " +
                "REMOVE f:Participant";

        tx.run(cypher, Map.of("familyId", family.getId().toString(), "eventId", event.getId().toString()));
        log.info("Event {} removed from family {}", event.getId(), family.getId());
    }
}
