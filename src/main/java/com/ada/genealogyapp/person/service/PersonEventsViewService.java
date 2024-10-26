package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.person.dto.PersonEventsResponse;
import com.ada.genealogyapp.person.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class PersonEventsViewService {


    private final PersonManagementService personManagementService;

    private final PersonSearchService personSearchService;

    public PersonEventsViewService(PersonManagementService personManagementService, PersonSearchService personSearchService) {
        this.personManagementService = personManagementService;
        this.personSearchService = personSearchService;
    }


    public PersonEventsResponse getPersonalEvents(UUID treeId, UUID personId) {
        personManagementService.validateTreeAndPerson(treeId, personId);
        Person person = personSearchService.findPersonByIdOrThrowNodeNotFoundException(personId);

        Set<EventRelationship> eventRelationships = person.getEvents();

        return PersonEventsResponse.entityToDtoMapper().apply(eventRelationships);
    }
}
