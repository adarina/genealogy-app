package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.person.dto.PersonEventRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PersonEventManagementService {

    private final PersonManagementService personManagementService;

    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final EventService eventService;

    public PersonEventManagementService(PersonManagementService personManagementService, TreeTransactionService treeTransactionService, EventManagementService eventManagementService, EventService eventService) {
        this.personManagementService = personManagementService;
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.eventService = eventService;
    }


    @TransactionalInNeo4j
    public void updatePersonalEvent(UUID treeId, UUID personId, UUID eventId, PersonEventRequest eventRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = personManagementService.validateTreePersonAndEvent(treeId, personId, eventId);

        eventManagementService.updateEventType(tx, event.getId(), eventRequest.getType());
        eventManagementService.updateDate(tx, event.getId(), eventRequest.getDate());
        eventManagementService.updatePlace(tx, event.getId(), eventRequest.getPlace());
        eventManagementService.updateDescription(tx, event.getId(), eventRequest.getDescription());

        String cypher = "MATCH (e:Event)-[r:HAS_PARTICIPANT]->(p:Participant) " +
                "WHERE e.id = $eventId AND p.id = $personId " +
                "SET r.relationship = $relationship " +
                "RETURN e, r";

        tx.run(cypher, Map.of(
                "eventId", eventId.toString(),
                "personId", personId.toString(),
                "relationship", eventRequest.getRelationship().toString()));

        log.info("Updated event {} for person {} with new relationship {}", eventId, personId, eventRequest.getRelationship());
        tx.commit();
    }

    @TransactionalInNeo4j
    public void removeEventFromPerson(UUID treeId, UUID personId, UUID eventId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Person person = personManagementService.validateTreeAndPerson(treeId, personId);
        Event event = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);

        String cypher = "MATCH (e:Event)-[r:HAS_PARTICIPANT]->(p:Participant) " +
                "WHERE e.id = $eventId AND p.id = $personId " +
                "DELETE r";

        tx.run(cypher, Map.of(
                "personId", person.getId().toString(),
                "eventId", event.getId().toString()));

        log.info("Event {} removed from person {}", event.getId(), person.getId());
        tx.commit();
    }
}

