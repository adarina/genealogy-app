package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.dto.PersonEventResponse;
import com.ada.genealogyapp.person.dto.PersonEventsResponse;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PersonEventsViewService {


    private final PersonManagementService personManagementService;

    private final PersonRepository personRepository;

    public PersonEventsViewService(PersonManagementService personManagementService, PersonRepository personRepository) {
        this.personManagementService = personManagementService;
        this.personRepository = personRepository;
    }


    public Page<PersonEventsResponse> getPersonalEvents(UUID treeId, UUID personId, Pageable pageable) {
        personManagementService.validateTreeAndPerson(treeId, personId);
        return personRepository.findPersonalEvents(personId, pageable);
    }

    public PersonEventResponse getPersonalEvent(UUID treeId, UUID personId, UUID eventId) {
        personManagementService.validateTreeAndPerson(treeId, personId);
        return personRepository.findPersonalEvent(eventId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString() + " and person " + personId.toString()));
    }
}