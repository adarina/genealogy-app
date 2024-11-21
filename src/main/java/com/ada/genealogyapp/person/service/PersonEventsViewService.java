package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonEventResponse;
import com.ada.genealogyapp.person.dto.PersonEventsResponse;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PersonEventsViewService {


    private final PersonRepository personRepository;

    private final TreeService treeService;

    private final PersonService personService;

    public PersonEventsViewService(PersonRepository personRepository, TreeService treeService, PersonService personService) {
        this.personRepository = personRepository;
        this.treeService = treeService;
        this.personService = personService;
    }

    public Page<PersonEventsResponse> getPersonalEvents(UUID treeId, UUID personId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        personService.ensurePersonExists(personId);
        return personRepository.findPersonalEvents(personId, pageable);
    }

    public PersonEventResponse getPersonalEvent(UUID treeId, UUID personId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        personService.ensurePersonExists(personId);
        return personRepository.findPersonalEvent(eventId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString() + " and person " + personId.toString()));
    }
}