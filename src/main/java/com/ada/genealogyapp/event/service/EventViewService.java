package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventFilterRequest;
import com.ada.genealogyapp.event.dto.EventsResponse;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;


@Service
@Slf4j
public class EventViewService {


    private final EventRepository eventRepository;
    private final TreeService treeService;

    public EventViewService(EventRepository eventRepository, TreeService treeService) {
        this.eventRepository = eventRepository;
        this.treeService = treeService;
    }

    public Page<EventsResponse> getEvents(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {

        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        ObjectMapper objectMapper = new ObjectMapper();
        EventFilterRequest filterRequest = objectMapper.readValue(filter, EventFilterRequest.class);

        String participants = filterRequest.getParticipants();
        String description = filterRequest.getDescription();
        EventType type = null;

        if (nonNull(filterRequest.getType())) {
            try {
                type = EventType.valueOf(filterRequest.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid type value: {}", filterRequest.getType());
            }
        }

        Page<EventsResponse> eventsPage;
        if (nonNull(type)) {
            eventsPage = eventRepository.findEventsResponseByTreeIdWithParticipantsAndDescriptionContainingIgnoreCaseAndParticipantsContainingIgnoreCaseAndTypeContaining(treeId, description != null ? description : "", participants != null ? participants : "", type, pageable);
        } else {
            eventsPage = eventRepository.findByTreeIdWithParticipantsAndDescriptionContainingIgnoreCaseAndParticipantsContainingIgnoreCase(treeId, description != null ? description : "", participants != null ? participants : "", pageable);
        }
        return eventsPage;
    }

    public EventResponse getEvent(UUID treeId, UUID eventId) {
        return eventRepository.findEventResponseByTreeIdAndEventId(treeId, eventId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString()));
    }
}