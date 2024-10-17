package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.service.EventCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/events")
public class EventCreationController {

    private final EventCreationService eventCreationService;

    public EventCreationController(EventCreationService eventCreationService) {
        this.eventCreationService = eventCreationService;
    }


    @PostMapping
    public ResponseEntity<?> createEvent(@PathVariable UUID treeId, @RequestBody EventRequest eventRequest) {
        eventCreationService.createEvent(treeId, eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
