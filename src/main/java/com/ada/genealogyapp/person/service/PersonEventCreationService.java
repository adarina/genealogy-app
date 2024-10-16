package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.exceptions.EventTypeApplicableException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PersonEventCreationService {

    private final PersonSearchService personSearchService;

    private final TreeRepository treeRepository;

    private final TreeSearchService treeSearchService;

    private final EventCreationService eventCreationService;

    public PersonEventCreationService(PersonSearchService personSearchService, TreeRepository treeRepository, TreeSearchService treeSearchService, EventCreationService eventCreationService) {
        this.personSearchService = personSearchService;
        this.treeRepository = treeRepository;
        this.treeSearchService = treeSearchService;
        this.eventCreationService = eventCreationService;
    }


    public void checkEvent(Event event) {
        if (!event.getEventType().getApplicableTo().equals("Person")) {
            throw new EventTypeApplicableException("This event type is not applicable to a person");
        }
    }

    public void createPersonEvent(UUID treeId, UUID personId, EventRequest eventRequest) {
        Event event = EventRequest.dtoToEntityMapper().apply(eventRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        Person person = personSearchService.findPersonById(personId);

        checkEvent(event);
        person.setTree(tree);

        eventCreationService.create(event);
        treeRepository.save(tree);

        log.info("Person event created successfully: {}", event.getId());
    }
}
