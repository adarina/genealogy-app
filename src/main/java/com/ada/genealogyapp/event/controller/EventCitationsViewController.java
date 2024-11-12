package com.ada.genealogyapp.event.controller;


import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.EventCitationsResponse;
import com.ada.genealogyapp.event.service.EventCitationsViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations")
public class EventCitationsViewController {

    private final EventCitationsViewService eventCitationsViewService;

    public EventCitationsViewController(EventCitationsViewService eventCitationsViewService) {
        this.eventCitationsViewService = eventCitationsViewService;
    }

    @GetMapping
    public ResponseEntity<Page<EventCitationsResponse>> getEventCitations(@PathVariable UUID treeId, @PathVariable UUID eventId, @PageableDefault Pageable pageable) {
        Page<EventCitationsResponse> citationResponses = eventCitationsViewService.getEventCitations(treeId, eventId, pageable);
        return ResponseEntity.ok(citationResponses);
    }

    @GetMapping("/{citationId}")
    public ResponseEntity<EventCitationResponse> getEventCitation(@PathVariable UUID treeId, @PathVariable UUID eventId, @PathVariable UUID citationId) {
        EventCitationResponse citationResponse = eventCitationsViewService.getEventCitation(treeId, eventId, citationId);
        return ResponseEntity.ok(citationResponse);
    }
}
