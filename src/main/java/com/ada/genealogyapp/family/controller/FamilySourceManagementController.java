package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.service.FamilySourceManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/sources")
public class FamilySourceManagementController {

    private final FamilySourceManagementService familySourceManagementService;

    private final CitationCreationService citationCreationService;

    public FamilySourceManagementController(FamilySourceManagementService familySourceManagementService, CitationCreationService citationCreationService) {
        this.familySourceManagementService = familySourceManagementService;
        this.citationCreationService = citationCreationService;
    }

    @PostMapping("/addExistingSource")
    public ResponseEntity<?> addSourceToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody UUIDRequest UUIDRequest) {
        familySourceManagementService.addSourceToFamily(treeId, familyId, UUIDRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewSource")
    public ResponseEntity<?> createAndAddSourceToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        UUIDRequest UUIDRequest = new UUIDRequest(citation.getId());
        familySourceManagementService.addSourceToFamily(treeId, familyId, UUIDRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removeSourceFromFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody UUIDRequest UUIDRequest) {
        familySourceManagementService.removeSourceFromFamily(treeId, familyId, UUIDRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
