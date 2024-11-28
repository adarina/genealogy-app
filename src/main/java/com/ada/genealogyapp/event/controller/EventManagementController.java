package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.service.EventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}")
public class EventManagementController {


    private final EventManagementService eventManagementService;

    public EventManagementController(EventManagementService eventManagementService) {
        this.eventManagementService = eventManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody EventRequest eventRequest) {
        eventManagementService.updateEvent(treeId, eventId, eventRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteEvent(@PathVariable String treeId, @PathVariable String eventId) {
        eventManagementService.deleteEvent(treeId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
