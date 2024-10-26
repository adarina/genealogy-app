package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.person.service.PersonEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events/{eventId}")
public class PersonEventManagementController {

    private final PersonEventManagementService personEventManagementService;

    private final CitationCreationService citationCreationService;

    public PersonEventManagementController(PersonEventManagementService personEventManagementService, CitationCreationService citationCreationService) {
        this.personEventManagementService = personEventManagementService;
        this.citationCreationService = citationCreationService;
    }


    @PutMapping()
    public ResponseEntity<?> updatePersonalEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody EventRequest eventRequest) {
        personEventManagementService.updatePersonalEvent(treeId, personId, eventId, eventRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addExistingCitation")
    public ResponseEntity<?> addCitationToPersonalEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody UUIDRequest UUIDRequest) {
        personEventManagementService.addCitationToPersonalEvent(treeId, personId, eventId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewCitation")
    public ResponseEntity<?> createAndAddCitationToPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        personEventManagementService.addCitationToPersonalEvent(treeId, personId, eventId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
