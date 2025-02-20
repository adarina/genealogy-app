package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventManagementService {

    private final EventService eventService;

    private final EventValidationService eventValidationService;

    @TransactionalInNeo4j
    public void updateEvent(String userId, String treeId, String eventId, @NonNull EventRequest eventRequest) {
        Event event = Event.builder()
                .description(eventRequest.getDescription())
                .place(eventRequest.getPlace())
                .date(eventRequest.getDate())
                .type(eventRequest.getType())
                .build();

        eventValidationService.validateEvent(event);
        eventService.updateEvent(userId, treeId, eventId, event);
    }

    @TransactionalInNeo4j
    public void updateEventWithParticipant(String userId, String treeId, String eventId, @NonNull ParticipantEventRequest eventRequest, String participantId) {
        Event event = Event.builder()
                .description(eventRequest.getDescription())
                .place(eventRequest.getPlace())
                .date(eventRequest.getDate())
                .type(eventRequest.getType())
                .build();

        eventValidationService.validateEvent(event);
        eventService.updateEventWithParticipant(userId, treeId, eventId, event, participantId, eventRequest.getRelationship().name());
    }

    @TransactionalInNeo4j
    public void removeParticipantFromEvent(String userId, String treeId, String participantId, String eventId) {
        eventService.removeParticipantFromEvent(userId, treeId, eventId, participantId);
    }

    @TransactionalInNeo4j
    public void deleteEvent(String userId, String treeId, String eventId) {
        eventService.deleteEvent(userId, treeId, eventId);
    }
}
