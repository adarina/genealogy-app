package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.family.service.FamilyEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventCreationController {
    private final FamilyEventManagementService familyEventManagementService;
    private final EventCreationService eventCreationService;

    public FamilyEventCreationController(FamilyEventManagementService familyEventManagementService, EventCreationService eventCreationService) {
        this.familyEventManagementService = familyEventManagementService;
        this.eventCreationService = eventCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddFamilyToEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody ParticipantEventRequest participantEventRequest) {
        Event event = eventCreationService.createEvent(treeId, participantEventRequest);
        familyEventManagementService.addFamilyToEvent(treeId, familyId, event.getId(), participantEventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}