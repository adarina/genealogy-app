package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}")
public class FamilyEventManagementController {

    private final EventManagementService eventManagementService;

    public FamilyEventManagementController(EventManagementService eventManagementService) {
        this.eventManagementService = eventManagementService;
    }


    @PutMapping
    public ResponseEntity<?> updateEventInFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEventWithParticipant(userId, treeId, eventId, participantEventRequest, familyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addFamilyToEvent(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.updateEventWithParticipant(userId, treeId, eventId, participantEventRequest, familyId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeFamilyFromEvent(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        eventManagementService.removeParticipantFromEvent(userId, treeId, familyId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

