package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationFileManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files/{fileId}")
public class CitationFileManagementController {

    private final CitationFileManagementService citationFileManagementService;

    public CitationFileManagementController(CitationFileManagementService citationFileManagementService) {
        this.citationFileManagementService = citationFileManagementService;
    }

    @PostMapping
    public ResponseEntity<?> addFileToCitation(@PathVariable UUID treeId, @PathVariable UUID citationId, @PathVariable UUID fileId) {
        citationFileManagementService.addFileToCitation(treeId, citationId, fileId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeFileFromCitation(@PathVariable UUID treeId, @PathVariable UUID citationId, @PathVariable UUID fileId) {
        citationFileManagementService.removeFileFromCitation(treeId, citationId, fileId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
