package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventCreationController {

    private final EventCreationService eventCreationService;

    public PersonEventCreationController(EventCreationService eventCreationService) {
        this.eventCreationService = eventCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddPersonToNewEvent(@PathVariable String treeId, @PathVariable String personId, @RequestBody ParticipantEventRequest participantEventRequest, @RequestHeader(value = "X-User-Id") String userId) {
        eventCreationService.createEventWithParticipant(userId,treeId, participantEventRequest, personId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}