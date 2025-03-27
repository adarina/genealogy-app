package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.CreateSourceRequestParams;
import com.ada.genealogyapp.source.service.SourceCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources")
public class SourceCreationController {

    private final SourceCreationService sourceCreationService;

    @PostMapping
    public ResponseEntity<?> createSource(@PathVariable String treeId, @RequestBody SourceRequest sourceRequest, @RequestHeader(value = "X-User-Id") String userId) {
        sourceCreationService.createSource(CreateSourceRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceRequest(sourceRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}