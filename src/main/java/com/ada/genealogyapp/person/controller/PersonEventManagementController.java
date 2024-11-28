package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.service.EventManagementService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events/{eventId}")
public class PersonEventManagementController {


    private final EventManagementService eventManagementService;

    public PersonEventManagementController(EventManagementService eventManagementService) {
        this.eventManagementService = eventManagementService;
    }


    @PutMapping
    public ResponseEntity<?> updateEventInPerson(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventManagementService.updateEvent(treeId, eventId, participantEventRequest, personId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeEventFromPerson(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId) {
        eventManagementService.removeParticipantFromEvent(treeId, personId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addPersonToEvent(@PathVariable String treeId, @PathVariable String personId, @PathVariable String eventId, @RequestBody ParticipantEventRequest participantEventRequest) {
        eventManagementService.updateEvent(treeId, eventId, participantEventRequest, personId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
