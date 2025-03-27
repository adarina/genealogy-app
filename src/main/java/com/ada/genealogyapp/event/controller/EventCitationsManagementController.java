package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.citation.dto.params.CreateAndAddCitationToEventRequestParams;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/citations")
public class EventCitationsManagementController {

    private final CitationCreationService citationCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddCitationToEvent(@PathVariable String treeId, @PathVariable String eventId, @RequestBody CitationRequest citationRequest, @RequestHeader(value = "X-User-Id") String userId) {
        citationCreationService.createAndAddCitationToEvent(CreateAndAddCitationToEventRequestParams.builder()
                .treeId(treeId)
                .userId(userId)
                .citationRequest(citationRequest)
                .eventId(eventId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}