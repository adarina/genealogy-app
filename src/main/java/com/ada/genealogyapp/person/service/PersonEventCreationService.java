package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PersonEventCreationService {

    private final PersonSearchService personSearchService;

    private final TreeService treeService;

    private final EventService eventService;

    public PersonEventCreationService(PersonSearchService personSearchService, TreeService treeService, EventService eventService) {
        this.personSearchService = personSearchService;
        this.treeService = treeService;
        this.eventService = eventService;
    }


    public void createPersonEvent(UUID treeId, UUID personId, EventRequest eventRequest) {
        Event event = EventRequest.dtoToEntityMapper().apply(eventRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        Person person = personSearchService.findPersonById(personId);

        eventService.checkEventApplicable(event, "Person");
        person.setTree(tree);

        eventService.saveEvent(event);
        treeService.saveTree(tree);

        log.info("Person event created successfully: {}", event.getId());
    }
}
