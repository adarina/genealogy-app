package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PersonEventsManagementService {

    private final PersonManagementService personManagementService;
    private final EventService eventService;
    private final TreeTransactionService treeTransactionService;

    public PersonEventsManagementService(PersonManagementService personManagementService, EventService eventService, TreeTransactionService treeTransactionService) {
        this.personManagementService = personManagementService;
        this.eventService = eventService;
        this.treeTransactionService = treeTransactionService;
    }

    @TransactionalInNeo4j
    public void addExistingEventToPerson(UUID treeId, UUID personId, UUID eventId, EventParticipantRelationshipType relationship) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Person person = personManagementService.validateTreeAndPerson(treeId, personId);
        Event event = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);

        for (Event existingEvent : person.getEvents()) {
            if (existingEvent.getId().equals(event.getId())) {
                throw new NodeAlreadyInNodeException("Event " + event.getId() + " is already part of the person " + personId);
            }
        }

        EventParticipantRelationshipType newRelationship = nonNull(relationship) ? relationship : EventParticipantRelationshipType.MAIN;
        String cypher = "MATCH (p:Person {id: $personId}) " +
                "MATCH (e:Event {id: $eventId}) " +
                "SET p:Participant " +
                "MERGE (e)-[:HAS_PARTICIPANT {relationship: $newRelationship}]->(p)";

        tx.run(cypher, Map.of(
                "personId", personId.toString(),
                "eventId", event.getId().toString(),
                "newRelationship", newRelationship.name()
        ));

        log.info("Event {} added successfully to the person {}", event.getType().toString(), person.getId());
        tx.commit();
    }
}
