package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.service.PersonEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventCreationController {

    private final EventCreationService eventCreationService;
    private final PersonEventManagementService personEventManagementService;

    public PersonEventCreationController(EventCreationService eventCreationService, PersonEventManagementService personEventManagementService) {
        this.eventCreationService = eventCreationService;
        this.personEventManagementService = personEventManagementService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddPersonToNewEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody ParticipantEventRequest participantEventRequest) {
        Event event = eventCreationService.createEvent(treeId, participantEventRequest);
        personEventManagementService.addPersonToEvent(treeId, personId, event.getId(), participantEventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}