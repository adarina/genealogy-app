package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.dto.PersonEventRequest;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FamilyEventManagementService {

    private final FamilyManagementService familyManagementService;

    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final EventService eventService;


    public FamilyEventManagementService(FamilyManagementService familyManagementService, TreeTransactionService treeTransactionService, EventManagementService eventManagementService, EventService eventService) {
        this.familyManagementService = familyManagementService;
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.eventService = eventService;
    }

    @TransactionalInNeo4j
    public void updateFamilyEvent(UUID treeId, UUID familyId, UUID eventId, PersonEventRequest eventRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = familyManagementService.validateTreeFamilyAndEvent(treeId, familyId, eventId);

        eventManagementService.updateEventType(tx, event.getId(), eventRequest.getType());
        eventManagementService.updateDate(tx, event.getId(), eventRequest.getDate());
        eventManagementService.updatePlace(tx, event.getId(), eventRequest.getPlace());
        eventManagementService.updateDescription(tx, event.getId(), eventRequest.getDescription());

        String cypher = "MATCH (e:Event)-[r:HAS_PARTICIPANT]->(p:Participant) " +
                "WHERE e.id = $eventId AND p.id = $familyId " +
                "SET r.relationship = $relationship " +
                "RETURN e, r";

        tx.run(cypher, Map.of(
                "eventId", eventId.toString(),
                "familyId", familyId.toString(),
                "relationship", eventRequest.getRelationship().toString()));

        log.info("Updated event {} for family {} with new relationship {}", eventId, familyId, eventRequest.getRelationship());
        tx.commit();
    }


    @TransactionalInNeo4j
    public void removeEventFromFamily(UUID treeId, UUID familyId, UUID eventId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Event event = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);

        String cypher = "MATCH (e:Event)-[r:HAS_PARTICIPANT]->(p:Participant) " +
                "WHERE e.id = $eventId AND p.id = $familyId " +
                "DELETE r";

        tx.run(cypher, Map.of(
                "familyId", family.getId().toString(),
                "eventId", event.getId().toString()));

        log.info("Event {} removed from family {}", event.getId(), family.getId());
        tx.commit();
    }
}
