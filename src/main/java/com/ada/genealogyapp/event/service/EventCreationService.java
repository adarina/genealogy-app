package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventCreationService {


    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    private final EventRepository eventRepository;

    public EventCreationService(TreeSearchService treeSearchService, TreeRepository treeRepository, EventRepository eventRepository) {
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
        this.eventRepository = eventRepository;
    }


    public Event create(Event event) {
        Event savedEvent = eventRepository.save(event);
        log.info("Event created successfully: {}", savedEvent);
        return savedEvent;
    }

    public void createEvent(UUID treeId) {
        Event event = new Event();

        Tree tree = treeSearchService.findTreeById(treeId);
        tree.getEvents().add(event);

        create(event);
        treeRepository.save(tree);
    }
}
