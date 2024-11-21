package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationSourceManagementService;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/source")
public class CitationSourceCreationController {

    private final SourceCreationService sourceCreationService;

    private final CitationSourceManagementService citationSourceManagementService;

    public CitationSourceCreationController(SourceCreationService sourceCreationService, CitationSourceManagementService citationSourceManagementService) {
        this.sourceCreationService = sourceCreationService;
        this.citationSourceManagementService = citationSourceManagementService;
    }

    @PostMapping
    public ResponseEntity<?> createAndAddSourceToCitation(@PathVariable UUID treeId, @PathVariable UUID citationId, @RequestBody SourceRequest sourceRequest) {
        Source source = sourceCreationService.createSource(treeId, sourceRequest);
        citationSourceManagementService.addSourceToCitation(treeId, citationId, source.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}