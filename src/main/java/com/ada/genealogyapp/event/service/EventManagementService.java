package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.params.*;
import com.ada.genealogyapp.event.dto.params.UpdateEventRequestWithParticipantParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventManagementService {

    private final EventService eventService;

    private final EventValidationService eventValidationService;

    public Event buildAndValidateEvent(UpdateEventRequestParams params) {
        Event event = Event.builder()
                .description(params.getEventRequest().getDescription())
                .place(params.getEventRequest().getPlace())
                .date(params.getEventRequest().getDate())
                .type(params.getEventRequest().getType())
                .build();
        eventValidationService.validateEvent(event);
        return event;
    }

    @TransactionalInNeo4j
    public void updateEvent(UpdateEventRequestParams params) {
        Event event = buildAndValidateEvent(params);
        eventService.updateEvent(UpdateEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(params.getEventId())
                .event(event)
                .build());
    }

    @TransactionalInNeo4j
    public void updateEventWithParticipant(UpdateEventRequestWithParticipantParams params) {
        Event event = buildAndValidateEvent(params);
        eventService.updateEventWithParticipant(UpdateEventWithParticipantParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(params.getEventId())
                .event(event)
                .participantId(params.getParticipantId())
                .relationshipType(params.getParticipantEventRequest().getRelationship().name())
                .build());
    }

    @TransactionalInNeo4j
    public void removeParticipantFromEvent(RemoveParticipantFromEventParams params) {
        eventService.removeParticipantFromEvent(params);
    }

    @TransactionalInNeo4j
    public void deleteEvent(DeleteEventParams params) {
        eventService.deleteEvent(params);
    }
}
