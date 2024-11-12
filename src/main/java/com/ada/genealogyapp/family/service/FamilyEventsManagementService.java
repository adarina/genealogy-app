package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventServiceImpl;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
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

    @TransactionalInNeo4j
    public void addEventToFamily(UUID treeId, UUID familyId, UUID eventId, EventParticipantRelationshipType relationship) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Event event = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);

        for (Event existingEvent : family.getEvents()) {
            if (existingEvent.getId().equals(event.getId())) {
                throw new NodeAlreadyInNodeException("Event " + event.getId() + " is already part of the family " + familyId);
            }
        }

        EventParticipantRelationshipType newRelationship = nonNull(relationship) ? relationship : EventParticipantRelationshipType.MAIN;
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (e:Event {id: $eventId}) " +
                "SET f:Participant " +
                "MERGE (e)-[:HAS_PARTICIPANT {relationship: $newRelationship}]->(f)";

        tx.run(cypher, Map.of(
                "familyId", familyId.toString(),
                "eventId", event.getId().toString(),
                "newRelationship", newRelationship.name()));

        log.info("Event {} added successfully to the family {}", event.getId(), family.getId());
        tx.commit();

    }
}
