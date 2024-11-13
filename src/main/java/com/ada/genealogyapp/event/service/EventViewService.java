package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventFilterRequest;
import com.ada.genealogyapp.event.dto.EventsResponse;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.repository.TreeRepository;
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
    private final TreeRepository treeRepository;
    private final ObjectMapper objectMapper;

    public EventViewService(EventRepository eventRepository, TreeRepository treeRepository, ObjectMapper objectMapper) {
        this.eventRepository = eventRepository;
        this.treeRepository = treeRepository;
        this.objectMapper = objectMapper;
    }

    public Page<EventsResponse> getEvents(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        treeRepository.findById(treeId).orElseThrow(() ->
                new NodeNotFoundException("Tree with ID " + treeId + " not found"));
        EventFilterRequest filterRequest = objectMapper.readValue(filter, EventFilterRequest.class);
        return eventRepository.findByTreeIdAndFilteredDescriptionParticipantNamesAndType(
                treeId,
                Optional.ofNullable(filterRequest.getDescription()).orElse(""),
                Optional.ofNullable(filterRequest.getParticipants()).orElse(""),
                Optional.ofNullable(filterRequest.getType()).orElse(""),
                pageable
        );
    }

    public EventResponse getEvent(UUID treeId, UUID eventId) {
        return eventRepository.findByTreeIdAndEventId(treeId, eventId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString()));
    }
}