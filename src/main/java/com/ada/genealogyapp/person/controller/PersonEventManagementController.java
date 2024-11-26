package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.service.PersonEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events/{eventId}")
public class PersonEventManagementController {

    private final PersonEventManagementService personEventManagementService;

    private final EventManagementService eventManagementService;

    public PersonEventManagementController(PersonEventManagementService personEventManagementService, EventManagementService eventManagementService) {
        this.personEventManagementService = personEventManagementService;
        this.eventManagementService = eventManagementService;
    }


    @PutMapping
    public ResponseEntity<?> updateEventInPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventManagementService.updateEvent(treeId, eventId, participantEventRequest, personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeEventFromPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId) {
        personEventManagementService.removeEventFromPerson(treeId, personId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addPersonToEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventManagementService.updateEvent(treeId, eventId, participantEventRequest, personId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
