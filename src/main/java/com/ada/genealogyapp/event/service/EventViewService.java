package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventFilterRequest;
import com.ada.genealogyapp.event.dto.EventPageResponse;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class EventViewService {

    private final EventRepository eventRepository;
    private final TreeService treeService;
    private final EventService eventService;
    private final ObjectMapper objectMapper;

    public EventViewService(EventRepository eventRepository, TreeService treeService, EventService eventService, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.treeService = treeService;
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }


    public Page<EventPageResponse> getEvents(String treeId, String filter, Pageable pageable) throws JsonProcessingException {
        treeService.ensureTreeExists(treeId);
        EventFilterRequest filterRequest = objectMapper.readValue(filter, EventFilterRequest.class);
        return eventRepository.findByTreeIdAndFilteredDescriptionParticipantNamesAndType(
                treeId,
                Optional.ofNullable(filterRequest.getDescription()).orElse(""),
                Optional.ofNullable(filterRequest.getParticipants()).orElse(""),
                Optional.ofNullable(filterRequest.getType()).orElse(""),
                pageable
        );
    }

    public EventResponse getEvent(String treeId, String eventId) {
        treeService.ensureTreeExists(treeId);
        eventService.ensureEventExists(eventId);
        return eventRepository.findByTreeIdAndEventId(treeId, eventId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString()));
    }
}