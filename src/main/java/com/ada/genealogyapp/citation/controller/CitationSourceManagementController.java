package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationSourceManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/source/{sourceId}")
public class CitationSourceManagementController {

    private final CitationSourceManagementService citationSourceManagementService;

    public CitationSourceManagementController(CitationSourceManagementService citationSourceManagementService) {
        this.citationSourceManagementService = citationSourceManagementService;
    }

    @DeleteMapping
    public ResponseEntity<?> removeSourceFromCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String sourceId, @RequestHeader(value = "X-User-Id") String userId) {
        citationSourceManagementService.removeSourceFromCitation(userId, treeId, citationId, sourceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addSourceToCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String sourceId, @RequestHeader(value = "X-User-Id") String userId) {
        citationSourceManagementService.addSourceToCitation(userId, treeId, citationId, sourceId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
