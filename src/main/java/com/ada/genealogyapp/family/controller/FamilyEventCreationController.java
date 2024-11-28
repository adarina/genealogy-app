package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventCreationController {
    private final EventCreationService eventCreationService;

    public FamilyEventCreationController(EventCreationService eventCreationService) {
        this.eventCreationService = eventCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddFamilyToEvent(@PathVariable String treeId, @PathVariable String familyId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventCreationService.createEvent(treeId, participantEventRequest, familyId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}