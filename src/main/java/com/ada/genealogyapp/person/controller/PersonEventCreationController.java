package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
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
    public ResponseEntity<?> addNewEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody EventRequest eventRequest) {
        Event event = eventCreationService.createEvent(treeId, eventRequest);
        personEventManagementService.addPersonToEvent(treeId, personId, event.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}