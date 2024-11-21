package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.service.EventCitationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations/{citationId}")
public class EventCitationManagementController {


    private final EventCitationManagementService eventCitationManagementService;

    public EventCitationManagementController(EventCitationManagementService eventCitationManagementService) {
        this.eventCitationManagementService = eventCitationManagementService;
    }

    @DeleteMapping()
    public ResponseEntity<?> removeCitationFromEvent(@PathVariable UUID treeId, @PathVariable UUID eventId, @PathVariable UUID citationId) {
        eventCitationManagementService.removeCitationFromEvent(treeId, eventId, citationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addCitationToEvent(@PathVariable UUID treeId, @PathVariable UUID eventId, @PathVariable UUID citationId) {
        eventCitationManagementService.addCitationToEvent(treeId, eventId, citationId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
