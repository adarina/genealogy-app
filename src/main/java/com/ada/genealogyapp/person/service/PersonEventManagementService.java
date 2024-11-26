package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.service.EventService;
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


    @TransactionalInNeo4j
    public void removeEventFromPerson(UUID treeId, UUID personId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Person person = personService.findPersonById(personId);
        Event event = eventService.findEventById(eventId);

        event.getParticipants().removeIf(rel -> rel.getParticipant().equals(person));
        eventService.saveEvent(event);
        log.info("Event {} removed from person {}", eventId, personId);
    }
}



