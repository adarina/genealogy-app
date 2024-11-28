package com.ada.genealogyapp.citation.controller;


import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.citation.service.CitationViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations")
public class CitationViewController {

    private final CitationViewService citationViewService;

    public CitationViewController(CitationViewService citationViewService) {
        this.citationViewService = citationViewService;
    }

    @GetMapping
    public ResponseEntity<Page<CitationSourceResponse>> getCitations(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<CitationSourceResponse> citationResponses = citationViewService.getCitations(treeId, filter, pageable);
        return ResponseEntity.ok(citationResponses);
    }

    @GetMapping("/{citationId}")
    public ResponseEntity<CitationSourceResponse> getCitation(@PathVariable String treeId, @PathVariable String citationId) {
        CitationSourceResponse citationResponse = citationViewService.getCitation(treeId, citationId);
        return ResponseEntity.ok(citationResponse);
    }
}
