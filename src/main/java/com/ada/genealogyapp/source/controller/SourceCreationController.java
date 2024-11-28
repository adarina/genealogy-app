package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.service.SourceCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources")
public class SourceCreationController {

    private final SourceCreationService sourceCreationService;

    public SourceCreationController(SourceCreationService sourceCreationService) {
        this.sourceCreationService = sourceCreationService;
    }


    @PostMapping
    public ResponseEntity<?> createSource(@PathVariable String treeId, @RequestBody SourceRequest sourceRequest) {
        sourceCreationService.createSource(treeId, sourceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
