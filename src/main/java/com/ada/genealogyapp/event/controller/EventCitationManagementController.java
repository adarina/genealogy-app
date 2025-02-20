package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.service.EventCitationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations/{citationId}")
public class EventCitationManagementController {


    private final EventCitationManagementService eventCitationManagementService;

    public EventCitationManagementController(EventCitationManagementService eventCitationManagementService) {
        this.eventCitationManagementService = eventCitationManagementService;
    }

    @DeleteMapping()
    public ResponseEntity<?> removeCitationFromEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String citationId, @RequestHeader(value = "X-User-Id") String userId) {
        eventCitationManagementService.removeCitationFromEvent(userId, treeId, eventId, citationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addCitationToEvent(@PathVariable String treeId, @PathVariable String eventId, @PathVariable String citationId, @RequestHeader(value = "X-User-Id") String userId) {
        eventCitationManagementService.addCitationToEvent(userId, treeId, eventId, citationId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
