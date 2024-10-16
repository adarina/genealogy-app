package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventSourceCreationService {

    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    private final EventCreationService eventCreationService;

    private final EventSearchService eventSearchService;

    public EventSourceCreationService(TreeSearchService treeSearchService, TreeRepository treeRepository, EventCreationService eventCreationService, EventSearchService eventSearchService) {
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
        this.eventCreationService = eventCreationService;
        this.eventSearchService = eventSearchService;
    }


    public void createEventSource(UUID treeId, UUID eventId, SourceRequest sourceRequest) {
        Source source = SourceRequest.dtoToEntityMapper().apply(sourceRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        Event event = eventSearchService.findEventById(eventId);

        source.setTree(tree);

        eventCreationService.create(event);
        treeRepository.save(tree);

        log.info("Event source created successfully: {}", event.getEventType());
    }
}
