package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Slf4j
public class FamilyManagementService {

    private final FamilySearchService familySearchService;
    private final TreeTransactionService treeTransactionService;
    private final TreeService treeService;

    private final EventService eventService;

    public FamilyManagementService(FamilySearchService familySearchService, TreeTransactionService treeTransactionService, TreeService treeService, EventService eventService) {
        this.familySearchService = familySearchService;
        this.treeTransactionService = treeTransactionService;
        this.treeService = treeService;
        this.eventService = eventService;
    }

    @Transactional
    public void startTransactionAndSession(UUID treeId, UUID familyId) {
        validateTreeAndFamily(treeId, familyId);
        treeTransactionService.startTransactionAndSession();
    }

    public Family validateTreeAndFamily(UUID treeId, UUID familyId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        return familySearchService.findFamilyByIdOrThrowNodeNotFoundException(familyId);
    }

    @Transactional
    public void startTransactionAndSession(UUID treeId, UUID familyId, UUID eventId) {
        validateTreeFamilyAndEvent(treeId, familyId, eventId);
        treeTransactionService.startTransactionAndSession();
    }

    public Event validateTreeFamilyAndEvent(UUID treeId, UUID familyId, UUID eventId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        familySearchService.findFamilyByIdOrThrowNodeNotFoundException(familyId);
        return eventService.findEventByIdOrThrowNodeNotFoundException(eventId);
    }
}