package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.EventCitationsResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventCitationsViewService {

    private final EventManagementService eventManagementService;

    private final EventRepository eventRepository;

    public EventCitationsViewService(EventManagementService eventManagementService, EventRepository eventRepository) {
        this.eventManagementService = eventManagementService;
        this.eventRepository = eventRepository;
    }

    public Page<EventCitationsResponse> getEventCitations(UUID treeId, UUID eventId, Pageable pageable) {
        eventManagementService.validateTreeAndEvent(treeId, eventId);
        return eventRepository.findEventCitations(eventId, pageable);
    }

    public EventCitationResponse getEventCitation(UUID treeId, UUID eventId, UUID citationId) {
        eventManagementService.validateTreeAndEvent(treeId, eventId);
        return eventRepository.findEventCitation(eventId, citationId)
                .orElseThrow(() -> new NodeNotFoundException("Citation " + citationId.toString() + " not found for tree " + treeId.toString() + " and event " + eventId.toString()));
    }
}
