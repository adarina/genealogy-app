package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.model.Event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class EventViewService {

    private final EventSearchService eventSearchService;

    public EventViewService(EventSearchService eventSearchService) {
        this.eventSearchService = eventSearchService;
    }

    public List<EventResponse> getEvents(UUID treeId) {
        List<Event> events = eventSearchService.getEventsByTreeIdOrThrowNodeNotFoundException(treeId);
        List<EventResponse> eventResponses = new ArrayList<>();

        for (Event event : events) {
            EventResponse response = new EventResponse();
            response.setId(event.getId());
            response.setType(event.getEventType());
            response.setDate(event.getDate());

            List<EventResponse.Participant> participants = event.getParticipants().stream()
                    .map(person -> new EventResponse.Participant(person.getParticipantId(), person.getParticipantName()))
                    .collect(Collectors.toList());
            response.setParticipants(participants);

            eventResponses.add(response);
        }
        return eventResponses;
    }
}
