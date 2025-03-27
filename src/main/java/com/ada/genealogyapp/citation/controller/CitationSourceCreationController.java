package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.CreateAndAddSourceToCitationRequestParams;
import com.ada.genealogyapp.source.service.SourceCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/source")
public class CitationSourceCreationController {

    private final SourceCreationService sourceCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddSourceToCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestBody SourceRequest sourceRequest, @RequestHeader(value = "X-User-Id") String userId) {
        sourceCreationService.createAndAddSourceToCitation(CreateAndAddSourceToCitationRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceRequest(sourceRequest)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}