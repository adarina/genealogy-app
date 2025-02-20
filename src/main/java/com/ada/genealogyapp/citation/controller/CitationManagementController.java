package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.service.CitationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}")
public class CitationManagementController {

    private final CitationManagementService citationManagementService;

    public CitationManagementController(CitationManagementService citationManagementService) {
        this.citationManagementService = citationManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestBody CitationRequest citationRequest, @RequestHeader(value = "X-User-Id") String userId) {
        citationManagementService.updateCitation(userId, treeId, citationId, citationRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestHeader(value = "X-User-Id") String userId) {
        citationManagementService.deleteCitation(userId, treeId, citationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
