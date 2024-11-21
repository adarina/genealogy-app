package com.ada.genealogyapp.citation.controller;


import com.ada.genealogyapp.citation.dto.CitationResponse;
import com.ada.genealogyapp.citation.dto.CitationsResponse;
import com.ada.genealogyapp.citation.service.CitationViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations")
public class CitationViewController {

    private final CitationViewService citationViewService;

    public CitationViewController(CitationViewService citationViewService) {
        this.citationViewService = citationViewService;
    }

    @GetMapping
    public ResponseEntity<Page<CitationsResponse>> getCitations(@PathVariable UUID treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<CitationsResponse> citationResponses = citationViewService.getCitations(treeId, filter, pageable);
        return ResponseEntity.ok(citationResponses);
    }

    @GetMapping("/{citationId}")
    public ResponseEntity<CitationResponse> getCitation(@PathVariable UUID treeId, @PathVariable UUID citationId) {
        CitationResponse citationResponse = citationViewService.getCitation(treeId, citationId);
        return ResponseEntity.ok(citationResponse);
    }
}
