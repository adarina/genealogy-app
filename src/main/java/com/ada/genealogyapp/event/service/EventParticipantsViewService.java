package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EventParticipantsViewService {

    private final TreeService treeService;

    private final EventService eventService;

    private final EventRepository eventRepository;

    public EventParticipantsViewService(TreeService treeService, EventService eventService, EventRepository eventRepository) {
        this.treeService = treeService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    public Page<EventParticipantResponse> getEventParticipants(String treeId, String eventId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        eventService.ensureEventExists(eventId);
        return eventRepository.findEventParticipants(treeId, eventId, pageable);
    }
}