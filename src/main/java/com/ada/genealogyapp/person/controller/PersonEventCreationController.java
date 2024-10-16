package com.ada.genealogyapp.person.controller;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.service.FamilyEventCreationService;
import com.ada.genealogyapp.person.service.PersonEventCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/persons/{personId}/events")
public class PersonEventCreationController {

    private final PersonEventCreationService personEventCreationService;

    public PersonEventCreationController(PersonEventCreationService personEventCreationService) {
        this.personEventCreationService = personEventCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createPersonEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody EventRequest eventRequest) {
        personEventCreationService.createPersonEvent(treeId, personId, eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}