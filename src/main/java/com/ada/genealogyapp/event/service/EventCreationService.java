package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
public class EventCreationService {


    private final TreeService treeService;

    private final EventService eventService;

    private final EventValidationService eventValidationService;

    public EventCreationService(TreeService treeService, EventService eventService, EventValidationService eventValidationService) {
        this.treeService = treeService;
        this.eventService = eventService;
        this.eventValidationService = eventValidationService;
    }

    @TransactionalInNeo4j
    public Event createEvent(UUID treeId, @NonNull EventRequest eventRequest) {
        Tree tree = treeService.findTreeById(treeId);

        Event event = Event.builder()
                .tree(tree)
                .type(eventRequest.getType())
                .place(eventRequest.getPlace())
                .description(eventRequest.getDescription())
                .date(eventRequest.getDate())
                .build();

        eventValidationService.validateEvent(event);

        eventService.saveEvent(event);
        return event;
    }
}