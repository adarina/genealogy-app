package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.relationship.EventParticipant;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.participant.service.ParticipantService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
public class EventCreationService {


    private final TreeService treeService;

    private final EventService eventService;

    private final ParticipantService participantService;

    private final EventValidationService eventValidationService;

    public EventCreationService(TreeService treeService, EventService eventService, ParticipantService participantService, EventValidationService eventValidationService) {
        this.treeService = treeService;
        this.eventService = eventService;
        this.participantService = participantService;
        this.eventValidationService = eventValidationService;
    }

    @TransactionalInNeo4j
    public void createEvent(UUID treeId, @NonNull EventRequest eventRequest) {
        Tree tree = treeService.findTreeById(treeId);

        Event event = Event.builder()
                .tree(tree)
                .type(eventRequest.getType())
                .place(eventRequest.getPlace())
                .description(eventRequest.getDescription())
                .date(eventRequest.getDate())
                .build();

        eventValidationService.validateEvent(event);
        eventService.saveEvent(event);
        log.info("Event created: {}", event);
    }

    @TransactionalInNeo4j
    public void createEvent(UUID treeId, @NonNull ParticipantEventRequest eventRequest, UUID participantId) {
        Tree tree = treeService.findTreeById(treeId);
        Participant participant = participantService.findParticipantById(participantId);

        EventParticipant relationship = EventParticipant.builder()
                .participant(participant)
                .relationship(eventRequest.getRelationship())
                .build();

        Event event = Event.builder()
                .tree(tree)
                .type(eventRequest.getType())
                .place(eventRequest.getPlace())
                .description(eventRequest.getDescription())
                .date(eventRequest.getDate())
                .participants(Set.of(relationship))
                .build();

        eventValidationService.validateEvent(event);
        eventService.saveEvent(event);
        log.info("Event with participant {} created: {}", participantId, event);
    }
}