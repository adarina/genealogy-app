package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.dto.FamilyEventResponse;
import com.ada.genealogyapp.family.service.FamilyEventsViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventsViewController {

    private final FamilyEventsViewService familyEventsViewService;

    public FamilyEventsViewController(FamilyEventsViewService familyEventsViewService) {
        this.familyEventsViewService = familyEventsViewService;
    }

    @GetMapping
    public ResponseEntity<Page<FamilyEventResponse>> getFamilyEvents(@PathVariable UUID treeId, @PathVariable UUID familyId, @PageableDefault Pageable pageable) {
        Page<FamilyEventResponse> eventResponses = familyEventsViewService.getFamilyEvents(treeId, familyId, pageable);
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<FamilyEventResponse> getFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId) {
        FamilyEventResponse eventResponse = familyEventsViewService.getFamilyEvent(treeId, familyId, eventId);
        return ResponseEntity.ok(eventResponse);
    }
}