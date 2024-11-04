package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
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
public class PersonEventManagementService {

    private final PersonManagementService personManagementService;

    private final TreeTransactionService treeTransactionService;

    private final EventManagementService eventManagementService;

    private final CitationService citationService;

    private final EventService eventService;

    public PersonEventManagementService(PersonManagementService personManagementService, TreeTransactionService treeTransactionService, EventManagementService eventManagementService, CitationService citationService, EventService eventService) {
        this.personManagementService = personManagementService;
        this.treeTransactionService = treeTransactionService;
        this.eventManagementService = eventManagementService;
        this.citationService = citationService;
        this.eventService = eventService;
    }


    @TransactionalInNeo4j
    public void updatePersonalEvent(UUID treeId, UUID personId, UUID eventId, EventRequest eventRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
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
        tx.commit();
    }

    @TransactionalInNeo4j
    public void addCitationToPersonalEvent(UUID treeId, UUID personId, UUID eventId, UUID citationId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Event event = personManagementService.validateTreePersonAndEvent(treeId, personId, eventId);

        Citation citation = citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);
        if (event.getCitations().contains(citation)) {
            throw new NodeAlreadyInNodeException("Citation " + citation.getId() + " is already part of the personal event " + eventId);
        }
        String cypher = "MATCH (e:Event {id: $eventId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (e)-[:HAS_CITATION]->(c)";

        tx.run(cypher, Map.of("eventId", eventId.toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the personal event {}", citation.getId(), event.getId().toString());
        tx.commit();
    }

    @TransactionalInNeo4j
    public void removeEventFromPerson(UUID treeId, UUID personId, UUID eventId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Person person = personManagementService.validateTreeAndPerson(treeId, personId);
        Event event = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);

        removeEventFromPerson(person, tx, event);
        tx.commit();
    }

    @TransactionalInNeo4j
    public void removeEventFromPerson(Person person, Transaction tx, Event event) {
        String cypher = "MATCH (p:Person {id: $personId}) " +
                "MATCH (e:Event {id: $eventId}) " +
                "MATCH (p)-[r1:HAS_EVENT]->(e) " +
                "MATCH (e)-[r2:HAS_PARTICIPANT]->(p) " +
                "DELETE r1, r2";

        tx.run(cypher, Map.of("personId", person.getId().toString(), "eventId", event.getId().toString()));
        log.info("Event {} removed from person {}", event.getId(), person.getId());
    }
}

