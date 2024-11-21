package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventManagementService {

    private final TreeService treeService;

    private final EventService eventService;

    private final EventRepository eventRepository;

    //TODO validation
    @TransactionalInNeo4j
    public void updateEvent(UUID treeId, UUID eventId, EventRequest eventRequest) {
        treeService.ensureTreeExists(treeId);
        eventService.ensureEventExists(eventId);

        eventRepository.updateEvent(eventId, eventRequest.getType(), eventRequest.getDate(), eventRequest.getPlace(), eventRequest.getDescription());
        log.info("Event updated successfully: {}", eventId);
    }

    @TransactionalInNeo4j
    public void deleteEvent(UUID treeId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);

        eventService.deleteEvent(event);
    }
}
