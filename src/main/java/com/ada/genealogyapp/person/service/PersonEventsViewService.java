package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.event.relationship.EventRelationship;
import com.ada.genealogyapp.person.dto.PersonEventsResponse;
import com.ada.genealogyapp.person.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonEventsViewService {


    private final PersonManagementService personManagementService;

    private final PersonSearchService personSearchService;


    public PersonEventsViewService(PersonManagementService personManagementService, PersonSearchService personSearchService) {
        this.personManagementService = personManagementService;
        this.personSearchService = personSearchService;
    }

    public Page<PersonEventsResponse> getPersonalEvents(UUID treeId, UUID personId, Pageable pageable) {
        personManagementService.validateTreeAndPerson(treeId, personId);
        Person person = personSearchService.findPersonByIdOrThrowNodeNotFoundException(personId);

        Set<EventRelationship> eventRelationships = person.getEvents();
        List<PersonEventsResponse> eventResponses = eventRelationships.stream()
                .map(eventRelationship -> {
                    PersonEventsResponse response = new PersonEventsResponse();
                    response.setId(eventRelationship.getEvent().getId());
                    response.setType(eventRelationship.getEvent().getEventType());
                    response.setDate(eventRelationship.getEvent().getDate());
                    response.setDescription(eventRelationship.getEvent().getDescription());
                    response.setPlace(eventRelationship.getEvent().getPlace());
                    response.setRelationship(eventRelationship.getEventRelationshipType());

                    List<PersonEventsResponse.Participant> participants = eventRelationship.getEvent().getParticipants().stream()
                            .map(participant -> new PersonEventsResponse.Participant(participant.getParticipantId(), participant.getParticipantName()))
                            .collect(Collectors.toList());
                    response.setParticipants(participants);

                    List<PersonEventsResponse.Citation> citations = eventRelationship.getEvent().getCitations().stream()
                            .map(citation -> new PersonEventsResponse.Citation(citation.getId()))
                            .collect(Collectors.toList());
                    response.setCitations(citations);

                    return response;
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), eventResponses.size());
        List<PersonEventsResponse> paginatedEvents = eventResponses.subList(start, end);

        return new PageImpl<>(paginatedEvents, pageable, eventResponses.size());
    }
}


