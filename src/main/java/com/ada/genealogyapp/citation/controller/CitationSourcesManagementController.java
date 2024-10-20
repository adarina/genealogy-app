package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationSourcesManagementService;
import com.ada.genealogyapp.person.dto.PersonUpdateRequest;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/sources")
public class CitationSourcesManagementController {

    private final CitationSourcesManagementService citationSourcesManagementService;
    private final SourceCreationService sourceCreationService;

    public CitationSourcesManagementController(CitationSourcesManagementService citationSourcesManagementService, SourceCreationService sourceCreationService) {
        this.citationSourcesManagementService = citationSourcesManagementService;
        this.sourceCreationService = sourceCreationService;
    }

    @PostMapping("/addExistingSource")
    public ResponseEntity<?> addExistingSource(@PathVariable UUID treeId, @PathVariable UUID citationId, @RequestBody PersonUpdateRequest personUpdateRequest) {
        citationSourcesManagementService.addExistingSourceToCitation(treeId, citationId, personUpdateRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewSource")
    public ResponseEntity<?> addNewSource(@PathVariable UUID treeId, @PathVariable UUID citationId, @RequestBody SourceRequest sourceRequest) {
        Source source = sourceCreationService.createSource(treeId, sourceRequest);
        citationSourcesManagementService.addExistingSourceToCitation(treeId, citationId, source.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
