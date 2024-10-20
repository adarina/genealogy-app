package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.service.FamilyEventManagementService;
import com.ada.genealogyapp.family.service.FamilyManagementService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}")
public class FamilyEventManagementController {

    private final FamilyEventManagementService familyEventManagementService;

    private final FamilyManagementService familyManagementService;

    private final TreeTransactionService treeTransactionService;

    private final CitationCreationService citationCreationService;

    public FamilyEventManagementController(FamilyEventManagementService familyEventManagementService, FamilyManagementService familyManagementService, TreeTransactionService treeTransactionService, CitationCreationService citationCreationService) {
        this.familyEventManagementService = familyEventManagementService;
        this.familyManagementService = familyManagementService;
        this.treeTransactionService = treeTransactionService;
        this.citationCreationService = citationCreationService;
    }

    @PostMapping()
    public ResponseEntity<Void> startEditingEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId) {
        familyManagementService.startTransactionAndSession(treeId, familyId, eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/commit")
    public ResponseEntity<?> updateFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody EventRequest eventRequest) {
        familyEventManagementService.updateFamilyEvent(treeId, familyId, eventId, eventRequest);
        treeTransactionService.commitChanges();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addExistingSource")
    public ResponseEntity<?> addSourceToFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody UUIDRequest UUIDRequest) {
        familyEventManagementService.addSourceToFamilyEvent(treeId, familyId, eventId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewSource")
    public ResponseEntity<?> createAndAddSourceToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        familyEventManagementService.addSourceToFamilyEvent(treeId, familyId, eventId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

