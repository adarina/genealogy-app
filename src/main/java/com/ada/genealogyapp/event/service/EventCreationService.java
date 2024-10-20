package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Slf4j
@Service
public class EventCreationService {


    private final TreeService treeService;

    private  final EventRepository eventRepository;

    public EventCreationService(TreeService treeService, EventRepository eventRepository) {
        this.treeService = treeService;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event createEvent(UUID treeId, EventRequest eventRequest) {
        Event event = EventRequest.dtoToEntityMapper().apply(eventRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        tree.getEvents().add(event);

        eventRepository.save(event);
        treeService.saveTree(tree);

        log.info("Event created successfully: {}", event.getEventType());

        return event;
    }
}