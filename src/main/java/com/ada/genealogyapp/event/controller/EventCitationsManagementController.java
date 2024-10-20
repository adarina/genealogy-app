package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.service.EventCitationsManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations")
public class EventCitationsManagementController {

    private final EventCitationsManagementService eventCitationsManagementService;

    private final CitationCreationService citationCreationService;

    public EventCitationsManagementController(EventCitationsManagementService eventCitationsManagementService, CitationCreationService citationCreationService) {
        this.eventCitationsManagementService = eventCitationsManagementService;
        this.citationCreationService = citationCreationService;
    }


    @PostMapping("/addExistingCitation")
    public ResponseEntity<?> addExistingCitation(@PathVariable UUID treeId, @PathVariable UUID eventId, @RequestBody EventCitationRequest eventCitationRequest) {
        eventCitationsManagementService.addExistingCitationToEvent(treeId, eventId, eventCitationRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewCitation")
    public ResponseEntity<?> addNewCitation(@PathVariable UUID treeId, @PathVariable UUID eventId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        eventCitationsManagementService.addExistingCitationToEvent(treeId, eventId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removeCitationFromEvent(@PathVariable UUID treeId, @PathVariable UUID eventId, @RequestBody EventCitationRequest eventCitationRequest) {
        eventCitationsManagementService.removeCitationFromEvent(treeId, eventId, eventCitationRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}