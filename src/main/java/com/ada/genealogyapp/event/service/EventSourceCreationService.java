package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventSourceCreationService {

    private final TreeService treeService;

    private final EventService eventService;

    public EventSourceCreationService(TreeService treeService, EventService eventService) {
        this.treeService = treeService;
        this.eventService = eventService;
    }

    public void createEventSource(UUID treeId, UUID eventId, SourceRequest sourceRequest) {
        Source source = SourceRequest.dtoToEntityMapper().apply(sourceRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        Event event = eventService.findEventByIdOrThrowNodeNotFoundException(eventId);

        source.setTree(tree);
        event.getSources().add(source);

        eventService.saveEvent(event);
        treeService.saveTree(tree);

        log.info("Event source created successfully: {}", event.getEventType());
    }
}
