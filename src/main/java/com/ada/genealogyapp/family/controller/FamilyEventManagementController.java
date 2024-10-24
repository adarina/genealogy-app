package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.service.FamilyEventManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}")
public class FamilyEventManagementController {

    private final FamilyEventManagementService familyEventManagementService;

    private final CitationCreationService citationCreationService;

    public FamilyEventManagementController(FamilyEventManagementService familyEventManagementService, CitationCreationService citationCreationService) {
        this.familyEventManagementService = familyEventManagementService;
        this.citationCreationService = citationCreationService;
    }

    @PutMapping()
    public ResponseEntity<?> updateFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody EventRequest eventRequest) {
        familyEventManagementService.updateFamilyEvent(treeId, familyId, eventId, eventRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addExistingCitation")
    public ResponseEntity<?> addCitationToFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody UUIDRequest UUIDRequest) {
        familyEventManagementService.addCitationToFamilyEvent(treeId, familyId, eventId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewSource")
    public ResponseEntity<?> createAndAddCitationToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        familyEventManagementService.addCitationToFamilyEvent(treeId, familyId, eventId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

