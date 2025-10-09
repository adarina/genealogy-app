package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.event.dto.params.AddParticipantAndLocationToEventParams;
import com.ada.genealogyapp.event.dto.params.AddParticipantToEventParams;
import com.ada.genealogyapp.event.dto.params.CreateEventRequestParams;
import com.ada.genealogyapp.event.dto.params.SaveEventParams;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantAndLocationParams;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.event.model.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventCreationService {

    private final EventService eventService;
    private final EventValidationService eventValidationService;

    private Event buildValidateAndSaveEvent(CreateEventRequestParams params) throws ValidationException {
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
    public Event createEvent(CreateEventRequestParams params) throws ValidationException {
        return buildValidateAndSaveEvent(params);
    }

    @TransactionalInNeo4j
    public Event createEventWithParticipantAndLocation(CreateEventRequestWithParticipantAndLocationParams params) throws ValidationException {
        Event event = buildValidateAndSaveEvent(params);
        eventService.addParticipantAndLocationToEvent(AddParticipantAndLocationToEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(event.getId())
                .locationId(params.getLocationId())
                .participantId(params.getParticipantId())
                .relationshipType(params.getParticipantEventRequest().getRelationship().name())
                .build());
        return event;
    }

    @TransactionalInNeo4j
    public Event createEventWithParticipant(CreateEventRequestWithParticipantParams params) throws ValidationException {
        Event event = buildValidateAndSaveEvent(params);
        eventService.addParticipantToEvent(AddParticipantToEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(event.getId())
                .participantId(params.getParticipantId())
                .relationshipType(params.getRelationshipType())
                .build());
        return event;
    }

    @TransactionalInNeo4j
    public Map<String, Event> createEvents(String userId, String treeId, List<EventJsonRequest> eventRequests) {
        Map<String, Event> createdEventsMap = new HashMap<>();
        List<Map<String, Object>> events = new ArrayList<>();

        for (EventJsonRequest request : eventRequests) {
            Event event = Event.builder()
                    .id(UUID.randomUUID().toString())
                    .type(request.getType())
                    .place(request.getPlace())
                    .description(request.getDescription())
                    .date(request.getDate())
                    .build();
            eventValidationService.validateEvent(event);

            Map<String, Object> eventData = new HashMap<>();
            eventData.put("id", event.getId());
            eventData.put("type", event.getType().name());
            eventData.put("description", event.getDescription());
            eventData.put("date", event.getDate());

            events.add(eventData);
            createdEventsMap.put(request.getId(), event);
        }
        eventService.saveEvents(userId, treeId, events);
        return createdEventsMap;
    }
}
