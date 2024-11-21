package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PersonEventManagementService {


    private final EventService eventService;

    private final TreeService treeService;

    private final PersonService personService;

    private final RelationshipManager relationshipManager;

    //TODO validation
    @TransactionalInNeo4j
    public void updateEventInPerson(UUID treeId, UUID personId, UUID eventId, EventRequest eventRequest) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);
        Event event = eventService.findEventById(eventId);

        relationshipManager.updateEventParticipantRelationship(event, person, eventRequest.getRelationship());
        log.info("Event {} relationship updated in person {}", eventId, personId);
    }

    @TransactionalInNeo4j
    public void removeEventFromPerson(UUID treeId, UUID personId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);
        Event event = eventService.findEventById(eventId);

        relationshipManager.removeEventParticipantRelationship(event, person);
        log.info("Event {} removed from person {}", eventId, personId);
    }

    @TransactionalInNeo4j
    public void addPersonToEvent(UUID treeId, UUID personId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);
        Event event = eventService.findEventById(eventId);

        if (event.isFamilyAlreadyInEvent(personId)) {
            throw new NodeAlreadyInNodeException("Person " + personId + " is already a participant of the event " + eventId);
        }

        relationshipManager.addEventParticipantRelationship(event, person, EventParticipantRelationshipType.MAIN);
        log.info("Person {} added successfully to the event {}", personId, eventId);
    }
}



