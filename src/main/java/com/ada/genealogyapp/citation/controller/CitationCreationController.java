package com.ada.genealogyapp.citation.controller;


import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/citations")
public class CitationCreationController {

    private final CitationCreationService citationCreationService;

    public CitationCreationController(CitationCreationService citationCreationService) {
        this.citationCreationService = citationCreationService;
    }


    @PostMapping
    public ResponseEntity<?> createCitation(@PathVariable UUID treeId, @RequestBody CitationRequest citationRequest) {
        citationCreationService.createCitation(treeId, citationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
