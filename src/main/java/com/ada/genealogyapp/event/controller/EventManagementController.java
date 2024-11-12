package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.service.EventManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}")
public class EventManagementController {


    private final EventManagementService eventManagementService;

    public EventManagementController(EventManagementService eventManagementService) {
        this.eventManagementService = eventManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateEvent(@PathVariable UUID treeId, @PathVariable UUID eventId, @RequestBody EventRequest eventRequest) {
        eventManagementService.updateEventData(treeId, eventId, eventRequest);
        return ResponseEntity.ok().build();
    }
}
