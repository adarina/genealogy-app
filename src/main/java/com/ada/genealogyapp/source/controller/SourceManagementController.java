package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.service.SourceManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources/{sourceId}")
public class SourceManagementController {

    private final SourceManagementService sourceManagementService;

    public SourceManagementController(SourceManagementService sourceManagementService) {
        this.sourceManagementService = sourceManagementService;
    }


    @PutMapping
    public ResponseEntity<?> updateSource(@PathVariable String treeId, @PathVariable String sourceId, @RequestBody SourceRequest sourceRequest) {
        sourceManagementService.updateSource(treeId, sourceId, sourceRequest);
        return ResponseEntity.ok().build();
    }
}

