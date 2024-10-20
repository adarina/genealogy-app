package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.person.service.PersonEventManagementService;
import com.ada.genealogyapp.person.service.PersonManagementService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events/{eventId}")
public class PersonEventManagementController {

    private final PersonEventManagementService personEventManagementService;

    private final PersonManagementService personManagementService;

    private final TreeTransactionService treeTransactionService;

    private final CitationCreationService citationCreationService;

    public PersonEventManagementController(PersonEventManagementService personEventManagementService, PersonManagementService personManagementService, TreeTransactionService treeTransactionService, CitationCreationService citationCreationService) {
        this.personEventManagementService = personEventManagementService;
        this.personManagementService = personManagementService;
        this.treeTransactionService = treeTransactionService;
        this.citationCreationService = citationCreationService;
    }

    @PostMapping()
    public ResponseEntity<Void> startEditingEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId) {
        personManagementService.startTransactionAndSession(treeId, personId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/commit")
    public ResponseEntity<?> updatePersonalEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody EventRequest eventRequest) {
        personEventManagementService.updatePersonalEvent(treeId, personId, eventId, eventRequest);
        treeTransactionService.commitChanges();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addExistingSource")
    public ResponseEntity<?> addSourceToPersonalEvent(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody UUIDRequest UUIDRequest) {
        personEventManagementService.addSourceToPersonalEvent(treeId, personId, eventId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewSource")
    public ResponseEntity<?> createAndAddSourceToPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @PathVariable UUID eventId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        personEventManagementService.addSourceToPersonalEvent(treeId, personId, eventId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
