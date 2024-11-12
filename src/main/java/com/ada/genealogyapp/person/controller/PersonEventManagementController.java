package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonEventRequest;
import com.ada.genealogyapp.person.service.PersonEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events/{eventId}")
public class PersonEventManagementController {

    private final PersonEventManagementService personEventManagementService;

    public PersonEventManagementController(PersonEventManagementService personEventManagementService) {
        this.personEventManagementService = personEventManagementService;
    }


    @PutMapping()
    public ResponseEntity<?> updatePersonalEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody PersonEventRequest personEventRequest) {
        personEventManagementService.updatePersonalEvent(treeId, personId, eventId, personEventRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removeEventFromPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId) {
        personEventManagementService.removeEventFromPerson(treeId, personId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
