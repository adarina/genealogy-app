package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.service.CitationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}")
public class CitationManagementController {

    private final CitationManagementService citationManagementService;

    public CitationManagementController(CitationManagementService citationManagementService) {
        this.citationManagementService = citationManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateCitation(@PathVariable UUID treeId, @PathVariable UUID citationId, @RequestBody CitationRequest citationRequest) {
        citationManagementService.updateCitation(treeId, citationId, citationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCitation(@PathVariable UUID treeId, @PathVariable UUID citationId) {
        citationManagementService.deleteCitation(treeId, citationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
