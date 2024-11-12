package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.person.dto.PersonUpdateRequest;
import com.ada.genealogyapp.person.service.PersonEventsManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventsManagementController {

    private final EventCreationService eventCreationService;
    private final PersonEventsManagementService personEventsManagementService;

    public PersonEventsManagementController(EventCreationService eventCreationService, PersonEventsManagementService personEventsManagementService) {
        this.eventCreationService = eventCreationService;
        this.personEventsManagementService = personEventsManagementService;
    }


    @PostMapping("/addExistingEvent")
    public ResponseEntity<?> addExistingEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody PersonUpdateRequest personUpdateRequest) {
        personEventsManagementService.addExistingEventToPerson(treeId, personId, personUpdateRequest.getId(), personUpdateRequest.getEventParticipantRelationshipType());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewEvent")
    public ResponseEntity<?> addNewEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody EventRequest eventRequest) {
        Event event = eventCreationService.createEvent(treeId, eventRequest);
        personEventsManagementService.addExistingEventToPerson(treeId, personId, event.getId(), eventRequest.getRelationship());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}