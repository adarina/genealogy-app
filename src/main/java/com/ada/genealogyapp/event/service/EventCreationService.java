package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
public class EventCreationService {

    private final EventService eventService;

    private final EventValidationService eventValidationService;

    public EventCreationService(EventService eventService, EventValidationService eventValidationService) {
        this.eventService = eventService;
        this.eventValidationService = eventValidationService;
    }

    @TransactionalInNeo4j
    public Event createEvent(String userId, String treeId, @NonNull EventRequest eventRequest) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type(eventRequest.getType())
                .place(eventRequest.getPlace())
                .description(eventRequest.getDescription())
                .date(eventRequest.getDate())
                .build();

        eventValidationService.validateEvent(event);
        eventService.saveEvent(userId, treeId, event);
        return event;
    }

    @TransactionalInNeo4j
    public void createEventWithParticipant(String userId, String treeId, @NonNull ParticipantEventRequest eventRequest, String participantId) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type(eventRequest.getType())
                .place(eventRequest.getPlace())
                .description(eventRequest.getDescription())
                .date(eventRequest.getDate())
                .build();

        eventValidationService.validateEvent(event);
        eventService.saveEventWithParticipant(userId, treeId, event, participantId, eventRequest.getRelationship().name());
    }

    @TransactionalInNeo4j
    public Event createEventWithParticipant(String userId, Tree tree, EventType type, String place, String description, String date, Participant participant, EventParticipantRelationshipType relationshipType) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type(type)
                .place(place)
                .description(description)
                .date(date)
                .build();

        eventValidationService.validateEvent(event);
        eventService.saveEventWithParticipant(userId, tree.getId(), event, participant.getId(), relationshipType.name());

        return event;
    }

    @TransactionalInNeo4j
    public Event createEvent(String userId, Tree tree, EventType type, String place, String description, String date) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type(type)
                .place(place)
                .description(description)
                .date(date)
                .build();

        eventValidationService.validateEvent(event);
        eventService.saveEvent(userId, tree.getId(), event);

        return event;
    }
}