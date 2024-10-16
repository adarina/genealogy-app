package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.exceptions.EventTypeApplicableException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class FamilyEventCreationService {

    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    private final FamilySearchService familySearchService;

    private final EventCreationService eventCreationService;

    private final FamilyRepository familyRepository;

    public FamilyEventCreationService(TreeSearchService treeSearchService, TreeRepository treeRepository, FamilySearchService familySearchService, EventCreationService eventCreationService, FamilyRepository familyRepository) {
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
        this.familySearchService = familySearchService;
        this.eventCreationService = eventCreationService;
        this.familyRepository = familyRepository;
    }

    public void checkEvent(Event event) {
        if (!event.getEventType().getApplicableTo().equals("Family")) {
            throw new EventTypeApplicableException("This event type is not applicable to a family");
        }
    }

    public void createFamilyEvent(UUID treeId, UUID familyId, EventRequest eventRequest) {
        Event event = EventRequest.dtoToEntityMapper().apply(eventRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        Family family = familySearchService.findFamilyById(familyId);

        checkEvent(event);

        family.setFamilyTree(tree);
        event.setTree(tree);

        family.getEvents().add(event);

        eventCreationService.create(event);
        treeRepository.save(tree);
        familyRepository.save(family);

        log.info("Family event created successfully: {}", event.getId());
    }
}
