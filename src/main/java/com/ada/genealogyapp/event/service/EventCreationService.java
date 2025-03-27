package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.params.AddParticipantToEventParams;
import com.ada.genealogyapp.event.dto.params.CreateEventRequestParams;
import com.ada.genealogyapp.event.dto.params.SaveEventParams;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventCreationService {

    private final EventService eventService;
    private final EventValidationService eventValidationService;

    private Event buildValidateAndSaveEvent(CreateEventRequestParams params) {
        Event event = Event.builder()
                .id(UUID.randomUUID().toString())
                .type(params.getEventRequest().getType())
                .place(params.getEventRequest().getPlace())
                .description(params.getEventRequest().getDescription())
                .date(params.getEventRequest().getDate())
                .build();
        eventValidationService.validateEvent(event);
        eventService.saveEvent(SaveEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(event.getId())
                .event(event)
                .build());
        return event;
    }

    @TransactionalInNeo4j
    public Event createEvent(CreateEventRequestParams params) {
        return buildValidateAndSaveEvent(params);
    }

    @TransactionalInNeo4j
    public Event createEventWithParticipant(CreateEventRequestWithParticipantParams params) {
        Event event = buildValidateAndSaveEvent(params);
        eventService.addParticipantToEvent(AddParticipantToEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(event.getId())
                .participantId(params.getParticipantId())
                .relationshipType(params.getParticipantEventRequest().getRelationship().name())
                .build());
        return event;
    }
}