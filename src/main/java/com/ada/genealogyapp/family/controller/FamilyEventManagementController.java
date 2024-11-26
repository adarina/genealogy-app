package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.family.service.FamilyEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}")
public class FamilyEventManagementController {

    private final FamilyEventManagementService familyEventManagementService;
    private final EventManagementService eventManagementService;

    public FamilyEventManagementController(FamilyEventManagementService familyEventManagementService, EventManagementService eventManagementService) {
        this.familyEventManagementService = familyEventManagementService;
        this.eventManagementService = eventManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateEventInFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventManagementService.updateEvent(treeId, eventId, participantEventRequest, familyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addFamilyToEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventManagementService.updateEvent(treeId, eventId, participantEventRequest, familyId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeFamilyFromEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId) {
        familyEventManagementService.removeFamilyFromEvent(treeId, familyId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

