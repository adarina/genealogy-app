package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationSourceManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/source/{sourceId}")
public class CitationSourceManagementController {

    private final CitationSourceManagementService citationSourceManagementService;

    public CitationSourceManagementController(CitationSourceManagementService citationSourceManagementService) {
        this.citationSourceManagementService = citationSourceManagementService;
    }

    @DeleteMapping
    public ResponseEntity<?> removeSourceFromCitation(@PathVariable UUID treeId, @PathVariable UUID citationId, @PathVariable UUID sourceId) {
        citationSourceManagementService.removeSourceFromCitation(treeId, citationId, sourceId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addSourceToCitation(@PathVariable UUID treeId, @PathVariable UUID citationId, @PathVariable UUID sourceId) {
        citationSourceManagementService.addSourceToCitation(treeId, citationId, sourceId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
