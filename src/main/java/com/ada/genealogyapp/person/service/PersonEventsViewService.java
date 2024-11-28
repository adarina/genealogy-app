package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.repository.ParticipantRepository;
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

    private final ParticipantRepository participantRepository;

    public PersonEventsViewService(PersonRepository personRepository, TreeService treeService, PersonService personService, ParticipantRepository participantRepository) {
        this.personRepository = personRepository;
        this.treeService = treeService;
        this.personService = personService;
        this.participantRepository = participantRepository;
    }

    public Page<ParticipantEventResponse> getPersonalEvents(String treeId, String personId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        personService.ensurePersonExists(personId);
        return participantRepository.findParticipantEvents(personId, pageable);
    }

    public ParticipantEventResponse getPersonalEvent(String treeId, String personId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        personService.ensurePersonExists(personId);
        return participantRepository.findParticipantEvent(eventId, personId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString() + " and person " + personId.toString()));
    }
}