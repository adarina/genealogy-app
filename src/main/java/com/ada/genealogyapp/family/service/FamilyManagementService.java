package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class FamilyManagementService {

    private final FamilySearchService familySearchService;
    private final TreeService treeService;
    private final EventService eventService;

    public FamilyManagementService(FamilySearchService familySearchService, TreeService treeService, EventService eventService) {
        this.familySearchService = familySearchService;
        this.treeService = treeService;
        this.eventService = eventService;
    }

    public Family validateTreeAndFamily(UUID treeId, UUID familyId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        return familySearchService.findFamilyByIdOrThrowNodeNotFoundException(familyId);
    }

    public Event validateTreeFamilyAndEvent(UUID treeId, UUID familyId, UUID eventId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        familySearchService.findFamilyByIdOrThrowNodeNotFoundException(familyId);
        return eventService.findEventByIdOrThrowNodeNotFoundException(eventId);
    }
}