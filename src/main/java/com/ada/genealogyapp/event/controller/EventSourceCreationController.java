package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.event.service.EventSourceCreationService;
import com.ada.genealogyapp.source.dto.SourceRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/events/{eventId}/sources")
public class EventSourceCreationController {

    private final EventSourceCreationService eventSourceCreationService;

    public EventSourceCreationController(EventSourceCreationService eventSourceCreationService) {
        this.eventSourceCreationService = eventSourceCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createEventSource(@PathVariable UUID treeId, @PathVariable UUID eventId, @RequestBody SourceRequest sourceRequest) {
        eventSourceCreationService.createEventSource(treeId, eventId, sourceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}