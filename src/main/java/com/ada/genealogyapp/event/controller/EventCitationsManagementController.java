package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.service.EventCitationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations")
public class EventCitationsManagementController {

    private final EventCitationManagementService eventCitationsManagementService;

    private final CitationCreationService citationCreationService;

    public EventCitationsManagementController(EventCitationManagementService eventCitationsManagementService, CitationCreationService citationCreationService) {
        this.eventCitationsManagementService = eventCitationsManagementService;
        this.citationCreationService = citationCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddCitationToEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        eventCitationsManagementService.addCitationToEvent(treeId, eventId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}