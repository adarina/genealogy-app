package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventsResponse;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.service.EventViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events")
public class EventViewController {

    private final EventViewService eventViewService;

    public EventViewController(EventViewService eventViewService) {
        this.eventViewService = eventViewService;
    }

    @GetMapping
    public ResponseEntity<Page<EventsResponse>> getEvents(@PathVariable UUID treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<EventsResponse> eventResponses = eventViewService.getEvents(treeId, filter, pageable);
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable UUID treeId, @PathVariable UUID eventId) {
        EventResponse eventResponse = eventViewService.getEvent(treeId, eventId);
        return ResponseEntity.ok(eventResponse);
    }
}
