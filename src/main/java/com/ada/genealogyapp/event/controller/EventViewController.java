package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.service.EventViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events")
public class EventViewController {

    private final EventViewService eventViewService;

    public EventViewController(EventViewService eventViewService) {
        this.eventViewService = eventViewService;
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getEvents(@PathVariable UUID treeId) {
        List<EventResponse> eventResponses = eventViewService.getEvents(treeId);
        return ResponseEntity.ok(eventResponses);
    }
}
