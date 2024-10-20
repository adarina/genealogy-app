package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}")
public class CitationManagementController {

    private final CitationManagementService citationManagementService;

    public CitationManagementController(CitationManagementService citationManagementService) {
        this.citationManagementService = citationManagementService;
    }

    @PostMapping()
    public ResponseEntity<Void> startEditingCitation(@PathVariable UUID treeId, @PathVariable UUID citationId) {
        citationManagementService.startTransactionAndSession(treeId, citationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



