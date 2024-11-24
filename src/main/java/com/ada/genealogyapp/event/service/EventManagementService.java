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

    private final EventValidationService eventValidationService;

    @TransactionalInNeo4j
    public void updateEvent(UUID treeId, UUID eventId, EventRequest eventRequest) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);

        event.setDescription(eventRequest.getDescription());
        event.setPlace(eventRequest.getPlace());
        event.setDate(eventRequest.getDate());
        event.setType(eventRequest.getType());

        eventValidationService.validateEvent(event);

        eventService.saveEvent(event);
    }

    @TransactionalInNeo4j
    public void deleteEvent(UUID treeId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);

        eventService.deleteEvent(event);
    }
}
