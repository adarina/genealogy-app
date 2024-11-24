package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventCitationsViewService {


    private final EventRepository eventRepository;
    private final TreeService treeService;
    private final EventService eventService;

    public EventCitationsViewService(EventRepository eventRepository, TreeService treeService, EventService eventService) {
        this.eventRepository = eventRepository;
        this.treeService = treeService;
        this.eventService = eventService;
    }

    public Page<EventCitationResponse> getEventCitations(UUID treeId, UUID eventId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        eventService.ensureEventExists(eventId);
        return eventRepository.findEventCitations(treeId, eventId, pageable);
    }

    public EventCitationResponse getEventCitation(UUID treeId, UUID eventId, UUID citationId) {
        treeService.ensureTreeExists(treeId);
        eventService.ensureEventExists(eventId);
        return eventRepository.findEventCitation(treeId, eventId, citationId)
                .orElseThrow(() -> new NodeNotFoundException("Citation " + citationId.toString() + " not found for tree " + treeId.toString() + " and event " + eventId.toString()));
    }
}