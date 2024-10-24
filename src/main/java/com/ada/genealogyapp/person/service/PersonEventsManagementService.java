package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.event.type.EventRelationshipType;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
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
    public void addExistingEventToPerson(UUID treeId, UUID personId, UUID eventId, EventRelationshipType eventRelationshipType) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Person person = personManagementService.validateTreeAndPerson(treeId, personId);
        Event event = eventService.findEventById(eventId);

        Set<EventRelationship> eventRelationships = person.getEvents();
        for (EventRelationship relationship : eventRelationships) {
            if (relationship.getEvent().getId().equals(event.getId())) {
                throw new NodeAlreadyInNodeException("Event " + event.getId() + " is already part of the person " + personId);
            }
        }

        EventRelationshipType type = nonNull(eventRelationshipType) ? eventRelationshipType : EventRelationshipType.MAIN;
        String cypher = "MATCH (p:Person {id: $personId}) " +
                "MATCH (e:Event {id: $eventId}) " +
                "MERGE (p)-[:HAS_EVENT {eventRelationshipType: $relationshipType}]->(e)";

        tx.run(cypher, Map.of("personId", personId.toString(), "eventId", event.getId().toString(), "relationshipType", type.name()));
        log.info("Event {} added successfully to the person {}", event.getEventType().toString(), person.getId());

        addDefaultHasParticipantToEvent(tx, person, event);
    }

    @TransactionalInNeo4j
    public void addDefaultHasParticipantToEvent(Transaction tx, Person person, Event event) {

        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (p:Person {id: $personId}) " +
                "SET f:Participant " +
                "MERGE (e)-[:HAS_PARTICIPANT]->(p)";

        tx.run(cypher, Map.of("personId", person.getId().toString(), "eventId", event.getId().toString()));
        log.info("Added person {} as participant to event {}", person.getId(), event.getId());
    }
}
