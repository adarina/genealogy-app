package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.dto.SourcesResponse;
import com.ada.genealogyapp.source.service.SourceViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources")
public class SourceViewController {

    private final SourceViewService sourceViewService;

    public SourceViewController(SourceViewService sourceViewService) {
        this.sourceViewService = sourceViewService;
    }


    @GetMapping
    public ResponseEntity<Page<SourcesResponse>> getSources(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<SourcesResponse> sourceResponses = sourceViewService.getSources(treeId, filter, pageable);
        return ResponseEntity.ok(sourceResponses);
    }

    @GetMapping("/{sourceId}")
    public ResponseEntity<SourceResponse> getSource(@PathVariable String treeId, @PathVariable String sourceId) {
        SourceResponse sourceResponse = sourceViewService.getSource(treeId, sourceId);
        return ResponseEntity.ok(sourceResponse);
    }
}
