package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.family.service.FamilyEventsManagementService;
import com.ada.genealogyapp.person.dto.PersonUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventsManagementController {
    private final FamilyEventsManagementService familyEventsManagementService;
    private final EventCreationService eventCreationService;

    public FamilyEventsManagementController(FamilyEventsManagementService familyEventsManagementService, EventCreationService eventCreationService) {
        this.familyEventsManagementService = familyEventsManagementService;
        this.eventCreationService = eventCreationService;
    }

    @PostMapping("/addExistingEvent")
    public ResponseEntity<?> addEventToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonUpdateRequest personUpdateRequest) {
        familyEventsManagementService.addEventToFamily(treeId, familyId, personUpdateRequest.getId(), personUpdateRequest.getEventParticipantRelationshipType());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewEvent")
    public ResponseEntity<?> createAndAddEventToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody EventRequest eventRequest) {
        Event event = eventCreationService.createEvent(treeId, eventRequest);
        familyEventsManagementService.addEventToFamily(treeId, familyId, event.getId(), eventRequest.getRelationship());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}