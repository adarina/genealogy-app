package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventCreationService {


    private final TreeService treeService;

    private final EventService eventService;

    public EventCreationService(TreeService treeService, EventService eventService) {
        this.treeService = treeService;
        this.eventService = eventService;
    }

    public void createEvent(UUID treeId, EventRequest eventRequest) {
//        if (eventRequest.getType() == null) {
//            throw new IllegalArgumentException("Event type must not be null");
//        }

        Event event = EventRequest.dtoToEntityMapper().apply(eventRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        tree.getEvents().add(event);

        eventService.saveEvent(event);
        treeService.saveTree(tree);

        log.info("Event created successfully: {}", event.getEventType());
    }
}